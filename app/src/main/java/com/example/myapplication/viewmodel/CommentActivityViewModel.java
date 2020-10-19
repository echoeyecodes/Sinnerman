package com.example.myapplication.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.example.myapplication.API.ApiUtils.ApiClient;
import com.example.myapplication.JobDispatchers.CommentDispatch;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Room.Dao.CommentDao;
import com.example.myapplication.Room.Dao.UserDao;
import com.example.myapplication.Room.PersistenceDatabase;
import com.example.myapplication.Utils.AuthUser;
import com.example.myapplication.util.AppHandlerThread;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.*;

public class CommentActivityViewModel extends AndroidViewModel {
    private final PersistenceDatabase persistenceDatabase;
    private final CommentDao commentDao;
    private final UserDao persist_user_dao;
    private final com.example.myapplication.API.DAO.CommentDao network_comment_dao;
    private final ApiClient apiClient;
    private AppHandlerThread appHandlerThread;
    private String video_id;
    private String message;
    private final CommentActivityThreadCustomHandler customHandler;
    private final MutableLiveData<NetworkState> request_status = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private HandlerThread handlerThread;

    public CommentActivityViewModel(@NonNull @NotNull Application application) {
        super(application);
        apiClient = ApiClient.getInstance(application);
        persistenceDatabase = PersistenceDatabase.getInstance(application);
        commentDao = persistenceDatabase.commentDao();
        persist_user_dao = persistenceDatabase.userDao();

        if(appHandlerThread == null){
            appHandlerThread = AppHandlerThread.getInstance();
            handlerThread = appHandlerThread.getHandlerThread();
        }
        network_comment_dao = apiClient.getClient(com.example.myapplication.API.DAO.CommentDao.class);
        customHandler = new CommentActivityThreadCustomHandler(handlerThread.getLooper(), network_comment_dao, persist_user_dao, this, commentDao);
        tempMethod();
    }

    public LiveData<List<CommentResponseBody>> getPersistedComments(){
        return commentDao.getComments();
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
        request_status.postValue(NetworkState.LOADING);
        fetchComments();
    }

    public void tempMethod(){
        Message message = Message.obtain(customHandler);
        message.what = 2;
        message.sendToTarget();
    }

    public MutableLiveData<Boolean> getIsRefreshing() {
        return isRefreshing;
    }

    public void onRefresh(){
        isRefreshing.setValue(true);
        fetchComments();
    }

    public void fetchComments(){
        Message message = Message.obtain(customHandler);
        message.what = 0;
        message.obj = video_id;
        message.sendToTarget();
    }

    public void persistComment(CommentModel commentModel){
        commentModel.setVideo_id(video_id);
        Message message = Message.obtain(customHandler);
        message.what = 1;
        message.obj = commentModel;
        message.sendToTarget();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MutableLiveData<NetworkState> getRequest_status() {
        return request_status;
    }

    private static class CommentActivityThreadCustomHandler extends Handler {

        private final CommentActivityViewModel commentActivityViewModel;
        private final com.example.myapplication.API.DAO.CommentDao commentDao;
        private final CommentDao persist_comment_dao;
        private final UserDao persist_user_dao;
        private final UserModel currentUser;

        public CommentActivityThreadCustomHandler(Looper looper, com.example.myapplication.API.DAO.CommentDao commentDao,UserDao persist_user_dao, CommentActivityViewModel commentActivityViewModel, CommentDao persist_comment_dao) {
            super(looper);
            this.commentActivityViewModel = commentActivityViewModel;
            this.commentDao = commentDao;
            this.persist_user_dao = persist_user_dao;
            this.persist_comment_dao = persist_comment_dao;

            AuthUser authUser = new AuthUser().getUser(commentActivityViewModel.getApplication());
            currentUser = new UserModel();
            currentUser.setUsername(authUser.getUsername());
            currentUser.setProfile_url(authUser.getProfile_url());
            currentUser.setFullname(authUser.getName());
            currentUser.setId(authUser.getId());
        }

        public void fetchComments(String id){
            Call<List<CommentResponseBody>> call =  commentDao.getComments(id);
            try {
                Response<List<CommentResponseBody>> response = call.execute();
                if(response.isSuccessful() && response.body() != null){
                    for(CommentResponseBody commentResponseBody : response.body()){
                        persist_comment_dao.insertCommentAndUser(commentResponseBody);
                    }
                    commentActivityViewModel.getRequest_status().postValue(NetworkState.SUCCESS);
                }
            } catch (IOException e) {
                e.printStackTrace();
                commentActivityViewModel.getRequest_status().postValue(NetworkState.ERROR);
                commentActivityViewModel.setMessage("Couldn't connect to server. Please try again");
            }
        }
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    fetchComments((String) msg.obj);
                    commentActivityViewModel.getIsRefreshing().postValue(false);
                    break;
                case 1:
                    CommentModel commentModel = (CommentModel) msg.obj;
                    commentModel.setUser_id(currentUser.getId());

                    CommentResponseBody commentResponseBody = new CommentResponseBody();
                    commentResponseBody.setComment((CommentModel) msg.obj);
                    commentResponseBody.setUser(currentUser);
                    persist_comment_dao.insertCommentAndUser(commentResponseBody);

                    WorkRequest workRequest = new OneTimeWorkRequest.Builder(CommentDispatch.class).build();
                    WorkManager workManager = WorkManager.getInstance(commentActivityViewModel.getApplication());
                    workManager.enqueue(workRequest);
                    break;
                case 2:
                    persist_comment_dao.deleteAllComment();
                    persist_user_dao.deleteAllUsers();
                    break;
            }
        }
    }

}

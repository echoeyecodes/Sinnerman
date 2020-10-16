package com.example.myapplication.JobDispatchers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.myapplication.API.ApiUtils.ApiClient;
import com.example.myapplication.API.DAO.CommentDao;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.Room.PersistenceDatabase;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class CommentDispatch extends Worker {

    private static CommentDao commentDao;
    private static com.example.myapplication.Room.Dao.CommentDao persist_comment_dao;
    private ApiClient apiClient;
    private PersistenceDatabase persistenceDatabase;

    public CommentDispatch(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        persistenceDatabase = PersistenceDatabase.getInstance(context);
        persist_comment_dao = persistenceDatabase.commentDao();
        apiClient = new ApiClient(context);
        commentDao = apiClient.getClient(CommentDao.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        boolean status = false;
        try {
            status = sendComments();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!status){
            return Result.retry();
        }
        return Result.success();
    }

    private boolean sendComments() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(new CommentRunnable());

        while (!future.isDone()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return future.get();
    }

    private static class CommentRunnable implements Callable<Boolean> {

        @Override
        public Boolean call() {
            boolean value = false;
            List<CommentModel> comments = persist_comment_dao.getUnsentComments();
            for (CommentModel commentModel : comments) {
                Call<CommentResponseBody> call = commentDao.sendComment(commentModel);
                try {
                    Response<CommentResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        persist_comment_dao.insertCommentAndUser(response.body());
                    }
                    value = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    value = false;
                }
            }
            return value;
        }
    }
}

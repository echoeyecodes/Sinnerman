package com.example.myapplication.Paging.DataSource;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;
import com.example.myapplication.API.ApiUtils.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.ViewModel.NetworkState;
import com.example.myapplication.util.AppHandlerThread;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class VideoDataSource extends ItemKeyedDataSource<Integer, VideoResponseBody> {

    private String message = "";
    private ApiClient apiClient;
    private static VideosDao videosDao;
    private HandlerThread handlerThread;
    private MutableLiveData<NetworkState> request_status;
    private MutableLiveData<List<VideoResponseBody>> videos = new MutableLiveData<>();
    private Handler handler;

    public VideoDataSource (Context context){
        apiClient = new ApiClient(context);
        videosDao = apiClient.getClient(VideosDao.class);

        request_status = new MutableLiveData<>(NetworkState.LOADING);

        if(handlerThread == null){
            handlerThread = AppHandlerThread.getInstance().getHandlerThread();
        }
        handler = new Handler(handlerThread.getLooper());

    }

    public MutableLiveData<NetworkState> getRequest_status() {
        return request_status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MutableLiveData<List<VideoResponseBody>> getVideos() {
        return videos;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<VideoResponseBody> callback) {
            handler.post(new FetchVideoHandler(this, 0, callback));
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<VideoResponseBody> callback) {
                Log.d("CARRR", ""+params.key);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<VideoResponseBody> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull VideoResponseBody item) {
        return null;
    }


    private static class FetchVideoHandler implements Runnable{
        private final VideoDataSource videoDataSource;
        private int offset;
        private LoadInitialCallback<VideoResponseBody> callback;

        public FetchVideoHandler(VideoDataSource videoDataSource, int offset, LoadInitialCallback<VideoResponseBody> callback){
            this.videoDataSource = videoDataSource;
            this.offset = offset;
            this.callback = callback;
        }


        @Override
        public void run() {
            Call<List<VideoResponseBody>> call = videosDao.fetchVideos("5", String.valueOf(offset));
            try {
                Response<List<VideoResponseBody>> response = call.execute();
                if(response.isSuccessful() && response.body() != null){
                    callback.onResult(response.body(), offset,response.body().size());
                    videoDataSource.getRequest_status().postValue(NetworkState.SUCCESS);
                }else{
                    videoDataSource.getRequest_status().postValue(NetworkState.ERROR);
                    videoDataSource.setMessage("An error occurred while retrieving videos");
                }
            } catch (IOException e) {
                e.printStackTrace();
                videoDataSource.getRequest_status().postValue(NetworkState.ERROR);
                videoDataSource.setMessage("Couldn't connect to server. Please try again");
            }
        }
    }

}

package com.example.myapplication.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoActivityViewModel extends ViewModel {
    private MutableLiveData<Boolean> isFullScreen = new MutableLiveData<>();

    public VideoActivityViewModel(){
        isFullScreen.setValue(false);
    }

    public MutableLiveData<Boolean> getIsFullScreen() {
        return isFullScreen;
    }

    public boolean getIsFullScreenValue(){
        assert isFullScreen.getValue() != null;
        return isFullScreen.getValue();
    }

    public void toggleFullScreen(boolean value){
        isFullScreen.setValue(value);
    }
}

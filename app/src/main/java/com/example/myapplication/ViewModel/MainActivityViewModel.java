package com.example.myapplication.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<Boolean> isFullScreen = new MutableLiveData<>();

    public MainActivityViewModel(){
        isFullScreen.setValue(false);
    }


    public LiveData<Boolean> getIsFullScreen() {
        return isFullScreen;
    }

    public void setIsFullScreen(boolean value) {
        this.isFullScreen.setValue(!value);
    }

}

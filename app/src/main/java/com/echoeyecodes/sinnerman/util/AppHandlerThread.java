package com.echoeyecodes.sinnerman.util;

import android.os.HandlerThread;

public class AppHandlerThread {
    private static AppHandlerThread appHandlerThread;
    private final HandlerThread handlerThread;

    private AppHandlerThread(){
        handlerThread = new HandlerThread("APP_HANDLER_THREAD");
        handlerThread.start();
    }

    public static synchronized AppHandlerThread getInstance(){
        if(appHandlerThread == null){
            appHandlerThread = new AppHandlerThread();
        }
        return appHandlerThread;
    }

    public HandlerThread getHandlerThread() {
        return handlerThread;
    }

    public void dispose(){
        handlerThread.quit();
        appHandlerThread = null;
    }

}

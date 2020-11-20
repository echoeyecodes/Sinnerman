package com.echoeyecodes.sinnerman.services

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService : JobService() {


    override fun onCreate() {
        super.onCreate()
        Log.d("CARRR", "SERIVCE CREATED")
    }

    override fun onDestroy() {
        Log.d("CARRR", "SERVICE STOPEED")
        super.onDestroy()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }
}
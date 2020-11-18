package com.echoeyecodes.sinnerman.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.echoeyecodes.sinnerman.MainActivity
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI()

        val authenticationManager = AuthenticationManager()

        val token = authenticationManager.checkToken(this)
        if (token == null || token == "") {
            authenticationManager.startAuthActivity(this)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            launchActivity(intent)
        }
    }

    private fun launchActivity(intent: Intent){
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            startActivity(intent)
            finish()
        }
    }



    @SuppressLint("InlinedApi")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

}
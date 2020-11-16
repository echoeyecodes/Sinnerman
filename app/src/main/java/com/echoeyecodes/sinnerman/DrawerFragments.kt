package com.echoeyecodes.sinnerman

import android.content.Context
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Interface.PrimaryFragmentContext

abstract class DrawerFragments : CustomFragment() {

    lateinit var mainActivityContext: MainActivityContext

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityContext = context as MainActivityContext
    }
}
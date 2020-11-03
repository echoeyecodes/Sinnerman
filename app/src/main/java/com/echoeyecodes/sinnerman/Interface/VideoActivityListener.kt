package com.echoeyecodes.sinnerman.Interface

import com.echoeyecodes.sinnerman.Models.ResolutionDimensions

interface VideoActivityListener {
    fun onResolutionSelected(position: Int, dimensions: ResolutionDimensions)

}
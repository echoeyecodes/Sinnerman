package com.example.myapplication.Interface

import com.example.myapplication.Models.ResolutionDimensions

interface VideoActivityListener {
    fun onResolutionSelected(position: Int, dimensions: ResolutionDimensions)
}
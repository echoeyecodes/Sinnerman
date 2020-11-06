package com.echoeyecodes.sinnerman.Utils

import androidx.recyclerview.widget.DiffUtil
import com.echoeyecodes.sinnerman.Models.VideoResponseBody

 class SealedClassDiffUtil<T : Any> : DiffUtil.ItemCallback<Result<T>>(){
    override fun areItemsTheSame(oldItem: Result<T>, newItem: Result<T>): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result<T>, newItem: Result<T>): Boolean {
        return oldItem == newItem
    }

}
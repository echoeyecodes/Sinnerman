package com.echoeyecodes.sinnerman.Utils

sealed class Result<out T : Any>{

    abstract val id:String
    data class Success<T : Any>(val data: T) : Result<T>(){
        override val id = data.toString()
    }
    object Loading : Result<Nothing>(){
        override val id = "Loading"
    }
    object Error : Result<Nothing>() {
        override val id = "Error"
    }
    object Refreshing : Result<Nothing>(){
        override val id = "Reshreshing"
    }

    object Idle : Result<Nothing>(){
        override val id = "Idle"
    }


}
package com.example.myapplication.Utils

class DurationConverter {

    companion object {
        fun getInstance() = DurationConverter()
    }

    fun convertToDuration(duration:String) : String{
        val new_duration =  duration.toDoubleOrNull()

        return if(new_duration != null){
            val formatted_minutes = "0".plus((new_duration/60).toInt())
            val formatted_seconds = "0".plus((new_duration%60).toInt())

            val minutes = formatted_minutes.substring(formatted_minutes.length - 2)
            val seconds = formatted_seconds.substring(formatted_seconds.length - 2)

            "$minutes:$seconds"
        }else{
            "N/A"
        }
    }
}
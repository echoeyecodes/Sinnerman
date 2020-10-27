package com.example.myapplication.Utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

class TimestampConverter {

    companion object {
        fun getInstance() = TimestampConverter()
    }

    @SuppressLint("SimpleDateFormat")
    fun convertToTimeDifference(time:String) : String{
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(time)
        val start = Date(dateformat!!.time).toInstant()
        val end = Instant.now()

        val duration = (Duration.between(start, end)).toMillis()
        return converter(duration)
    }

    private fun converter(date: Long): String {
        val days = date / 1000 / 60 / 60 / 24
        val hours = date / 1000 / 60 / 60
        val minutes = date / 1000 / 60
        val seconds = date / 1000

        return when {
            days >= 1 -> {
                "$days days ago"
            }
            hours >= 1 -> {
                "$hours hours ago"
            }
            minutes >= 1 -> {
                "$minutes minutes ago"
            }
            seconds > 0 -> {
                "a few seconds ago"
            }
            else -> {
                "a while ago"
            }
        }
    }
}
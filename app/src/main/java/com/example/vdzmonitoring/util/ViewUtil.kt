package com.example.vdzmonitoring.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

fun now() = Timestamp(System.currentTimeMillis()).toString()

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).also { snackbar ->
        snackbar.setAction("Ok")  {
            snackbar.dismiss()
        }
    }.show()
}


fun <T : Activity?>  Context.changeActivity(cls: Class<T>, clearStack: Boolean = true, param: Pair<String, Long> = Pair("", 0L)) {
    Intent(this, cls).also {
        if(clearStack)
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        if(param != Pair("", 0L))
            it.putExtra(param.first, param.second)
        startActivity(it)
    }
}

fun Double.round(numFractionDigits: Int = 2) =
    String.format("%.${numFractionDigits}f", toDouble())

fun Long.toTime(): String {
    val format = "%02d:%02d:%02d"
    val hh = TimeUnit.MILLISECONDS.toHours(this)
    val mm = TimeUnit.MILLISECONDS.toMinutes(this).rem(60)
    val ss = TimeUnit.MILLISECONDS.toSeconds(this).rem(60)

    return String.format(format, hh, mm, ss)
}

@SuppressLint("SimpleDateFormat")
fun Long.toDateTime(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    return formatter.format(this)
}
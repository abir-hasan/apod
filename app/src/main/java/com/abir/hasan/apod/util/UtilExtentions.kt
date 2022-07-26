package com.abir.hasan.apod.util

import android.util.Log
import com.abir.hasan.apod.BuildConfig

const val APP_TAG = "Assignment"

fun String.logVerbose(tag: String = APP_TAG) {
    if (BuildConfig.DEBUG)
        Log.v(tag, this)
}

fun String.logDebug(tag: String = APP_TAG) {
    if (BuildConfig.DEBUG)
        Log.d(tag, this)
}

fun String.logInfo(tag: String = APP_TAG) {
    if (BuildConfig.DEBUG)
        Log.i(tag, this)
}

fun String.logWarn(tag: String = APP_TAG) {
    if (BuildConfig.DEBUG)
        Log.w(tag, this)
}

fun String.logError(tag: String = APP_TAG) {
    if (BuildConfig.DEBUG)
        Log.e(tag, this)
}
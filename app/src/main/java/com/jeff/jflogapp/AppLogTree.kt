package com.jeff.jflogapp

import android.util.Log
import com.log.JFLog

class AppLogTree: JFLog.LogTree() {
    override fun isLoggable(): Boolean {
        return BuildConfig.DEBUG || Log.isLoggable("app", Log.DEBUG)
    }

    override fun log(level: JFLog.LogLevel, tag: String, message: String): String? {
        // val newLevel = JFLog.LogLevel.WARN
        val newTag = "App"
        return super.log(level, newTag, message)
    }
}
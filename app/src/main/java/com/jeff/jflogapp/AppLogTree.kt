package com.jeff.jflogapp

import com.log.JFLog

class AppLogTree: JFLog.LogTree() {

    override fun log(level: JFLog.LogLevel, tag: String, message: String) {
        // val newLevel = JFLog.LogLevel.WARN
        val newTag = "App"
        super.log(level, newTag, message)
    }
}
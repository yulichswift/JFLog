package com.log

import android.util.Log

class JFLog {
    enum class LogLevel {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }

    companion object {
        // private const val MAX_LOG_LENGTH = 4000
        // private const val MAX_TAG_LENGTH = 23

        private var LINE_LENGTH = 500
        private var GLOBAL_TAG = "JFLog"

        fun setGlobalTag(tag: String) {
            GLOBAL_TAG = tag
        }

        fun setLineLength(length: Int) {
            LINE_LENGTH = length
        }

        /**
         * Verbose
         */
        fun v(message: String) {
            v(GLOBAL_TAG, 1, message)
        }

        /**
         * Custom verbose
         */
        fun v(tag: String, hierarchy: Int, message: String) {
            myLog(LogLevel.VERBOSE, tag, hierarchy + 1, message)
        }

        /**
         * Debug
         */
        fun d(message: String) {
            d(GLOBAL_TAG, 1, message)
        }

        /**
         * Custom debug
         */
        fun d(tag: String, hierarchy: Int, message: String) {
            myLog(LogLevel.DEBUG, tag, hierarchy + 1, message)
        }

        /**
         * Info
         */
        fun i(message: String) {
            i(GLOBAL_TAG, 1, message)
        }

        /**
         * Custom info
         */
        fun i(tag: String, hierarchy: Int, message: String) {
            myLog(LogLevel.INFO, tag, hierarchy + 1, message)
        }

        /**
         * Warn
         */
        fun w(message: String) {
            w(GLOBAL_TAG, 1, message)
        }

        /**
         * Custom warn
         */
        fun w(tag: String, hierarchy: Int, message: String) {
            myLog(LogLevel.WARN, tag, hierarchy + 1, message)
        }

        /**
         * Error
         */
        fun e(message: String) {
            e(GLOBAL_TAG, 1, message)
        }

        /**
         * Custom error
         */
        fun e(tag: String, hierarchy: Int, message: String) {
            myLog(LogLevel.ERROR, tag, hierarchy + 1, message)
        }

        /**
         * Assert
         */
        fun wtf(message: String) {
            wtf(GLOBAL_TAG, 1, message)
        }

        /**
         * Custom assert
         */
        fun wtf(tag: String, hierarchy: Int, message: String) {
            myLog(LogLevel.ASSERT, tag, hierarchy + 1, message)
        }

        private fun loggable(): Boolean {
            if (BuildConfig.DEBUG || Log.isLoggable("jflog", Log.DEBUG)) {
                return true
            }

            return false
        }

        private fun getPrefix(trace: StackTraceElement): String {
            return "${getLinkLog(trace)}[${trace.methodName}]"
        }

        private fun getLinkLog(trace: StackTraceElement): String {
            return "(${trace.fileName}:${trace.lineNumber})"
        }

        private fun myLog(level: LogLevel, tag: String, hierarchy: Int, message: String) {
            if (loggable()) {
                val trace = Throwable().stackTrace[hierarchy + 1]
                val logPrefix = getPrefix(trace)

                if (message.length > LINE_LENGTH) {
                    var isFirst = true
                    var remainMessage = message
                    while (remainMessage.length > LINE_LENGTH) {
                        remainMessage.subSequence(0, LINE_LENGTH).also {
                            if (isFirst) {
                                isFirst = false
                                myPrint(level, tag, "$logPrefix↘: $it")
                            } else {
                                myPrint(level, tag, "$logPrefix↗: $it")
                            }
                        }

                        remainMessage = remainMessage.substring(LINE_LENGTH)
                    }

                    if (remainMessage.isNotEmpty()) {
                        myPrint(level, tag, "$logPrefix↗: $remainMessage")
                    }
                } else {
                    myPrint(level, tag, "$logPrefix: $message")
                }
            }
        }

        private fun myPrint(level: LogLevel, tag: String, message: String) {
            when (level) {
                LogLevel.VERBOSE -> Log.v(tag, message)
                LogLevel.DEBUG -> Log.d(tag, message)
                LogLevel.INFO -> Log.i(tag, message)
                LogLevel.WARN -> Log.w(tag, message)
                LogLevel.ERROR -> Log.e(tag, message)
                LogLevel.ASSERT -> Log.wtf(tag, message)
            }
        }
    }
}
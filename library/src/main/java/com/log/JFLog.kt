package com.log

import android.util.Log

class JFLog {
    enum class LogLevel {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }

    interface BaseTree {
        fun log(level: LogLevel, tag: String, message: String)
    }

    abstract class LogTree : BaseTree {
        override fun log(level: LogLevel, tag: String, message: String) =
            myLog3(level, tag, message)
    }

    companion object {
        // private const val MAX_LOG_LENGTH = 4000
        // private const val MAX_TAG_LENGTH = 23

        private var mSingleLineLength = 500
        private var mGlobalTag = "JFLog"
        private var mLogTree: LogTree? = null

        fun setGlobalTag(tag: String) {
            mGlobalTag = tag
        }

        fun setLineLength(length: Int) {
            mSingleLineLength = length
        }

        fun setLogTree(logTree: LogTree) {
            mLogTree = logTree
        }

        /**
         * VERBOSE
         */
        fun v(message: String, tag: String = mGlobalTag, hierarchy: Int = 1) {
            myLog1(LogLevel.VERBOSE, tag, hierarchy + 1, message)
        }

        /**
         * Debug
         */
        fun d(message: String, tag: String = mGlobalTag, hierarchy: Int = 1) {
            myLog1(LogLevel.DEBUG, tag, hierarchy + 1, message)
        }

        /**
         * Info
         */
        fun i(message: String, tag: String = mGlobalTag, hierarchy: Int = 1) {
            myLog1(LogLevel.INFO, tag, hierarchy + 1, message)
        }

        /**
         * Warn
         */
        fun w(message: String, tag: String = mGlobalTag, hierarchy: Int = 1) {
            myLog1(LogLevel.WARN, tag, hierarchy + 1, message)
        }

        /**
         * Error
         */
        fun e(message: String, tag: String = mGlobalTag, hierarchy: Int = 1) {
            myLog1(LogLevel.ERROR, tag, hierarchy + 1, message)
        }

        /**
         * Assert
         */
        fun wtf(message: String, tag: String = mGlobalTag, hierarchy: Int = 1) {
            myLog1(LogLevel.ASSERT, tag, hierarchy + 1, message)
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

        private fun myLog1(level: LogLevel, tag: String, hierarchy: Int, message: String) {
            if (loggable()) {
                val trace = Throwable().stackTrace[hierarchy + 1]
                val logPrefix = getPrefix(trace)

                if (message.length > mSingleLineLength) {
                    var isFirst = true
                    var remainMessage = message
                    while (remainMessage.length > mSingleLineLength) {
                        remainMessage.subSequence(0, mSingleLineLength).also {
                            if (isFirst) {
                                isFirst = false
                                myLog2(level, tag, "$logPrefix↘: $it")
                            } else {
                                myLog2(level, tag, "$logPrefix↗: $it")
                            }
                        }

                        remainMessage = remainMessage.substring(mSingleLineLength)
                    }

                    if (remainMessage.isNotEmpty()) {
                        myLog2(level, tag, "$logPrefix↗: $remainMessage")
                    }
                } else {
                    myLog2(level, tag, "$logPrefix: $message")
                }
            }
        }

        private fun myLog2(level: LogLevel, tag: String, message: String) {
            if (mLogTree == null) {
                myLog3(level, tag, message)
            } else {
                mLogTree?.log(level, tag, message)
            }
        }

        private fun myLog3(level: LogLevel, tag: String, message: String) {
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
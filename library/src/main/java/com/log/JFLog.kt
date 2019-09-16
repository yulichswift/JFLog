package com.log

import android.util.Log

class JFLog {
    enum class LogLevel {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }

    interface BaseTree {
        fun isLoggable(): Boolean
        fun log(level: LogLevel, tag: String, message: String)
    }

    abstract class LogTree : BaseTree {
        override fun log(level: LogLevel, tag: String, message: String) =
            prepareLog3(level, tag, message)
    }

    companion object {
        private const val MAX_TAG_LENGTH = 23
        private const val DEFAULT = 1

        private var mMaxLineLength = 500
        private var mGlobalTag = "JFLog"
        private var mEnableLogTree = false
        private var mLogTree: LogTree? = null
        private val mPropertyLoggable by lazy { Log.isLoggable("jflog", Log.DEBUG) }

        var isLoggable = true

        fun setGlobalTag(tag: String) {
            if (tag.length > MAX_TAG_LENGTH) {
                mGlobalTag = tag.substring(0, MAX_TAG_LENGTH)
            } else {
                mGlobalTag = tag
            }
        }

        fun getGlobalTag() = mGlobalTag

        fun setGlobalMaxLineLength(length: Int) {
            mMaxLineLength = length
        }

        fun setGlobalLogTree(logTree: LogTree?) {
            mEnableLogTree = logTree != null
            mLogTree = logTree
        }

        /**
         * Verbose
         */
        fun v(message: String) {
            v(getGlobalTag(), 1, message)
        }

        /**
         * Verbose
         */
        fun v(tag: String, message: String) {
            v(tag, 1, message)
        }

        /**
         * Verbose
         */
        fun v(hierarchy: Int, message: String) {
            v(getGlobalTag(), hierarchy + 1, message)
        }

        /**
         * Verbose
         */
        fun v(tag: String = getGlobalTag(), hierarchy: Int = DEFAULT, message: String) {
            prepareLog1(LogLevel.VERBOSE, tag, hierarchy + 1, message)
        }

        /**
         * Debug
         */
        fun d(message: String) {
            d(getGlobalTag(), 1, message)
        }

        /**
         * Debug
         */
        fun d(tag: String, message: String) {
            d(tag, 1, message)
        }

        /**
         * Debug
         */
        fun d(hierarchy: Int, message: String) {
            d(getGlobalTag(), hierarchy + 1, message)
        }

        /**
         * Debug
         */
        fun d(tag: String = getGlobalTag(), hierarchy: Int = DEFAULT, message: String) {
            prepareLog1(LogLevel.DEBUG, tag, hierarchy + 1, message)
        }

        /**
         * Info
         */
        fun i(message: String) {
            i(getGlobalTag(), 1, message)
        }

        /**
         * Info
         */
        fun i(tag: String, message: String) {
            i(tag, 1, message)
        }

        /**
         * Info
         */
        fun i(hierarchy: Int, message: String) {
            i(getGlobalTag(), hierarchy + 1, message)
        }

        /**
         * Info
         */
        fun i(tag: String = getGlobalTag(), hierarchy: Int = DEFAULT, message: String) {
            prepareLog1(LogLevel.INFO, tag, hierarchy + 1, message)
        }

        /**
         * Warn
         */
        fun w(message: String) {
            w(getGlobalTag(), 1, message)
        }

        /**
         * Warn
         */
        fun w(tag: String, message: String) {
            w(tag, 1, message)
        }

        /**
         * Warn
         */
        fun w(hierarchy: Int, message: String) {
            w(getGlobalTag(), hierarchy + 1, message)
        }

        /**
         * Warn
         */
        fun w(tag: String = getGlobalTag(), hierarchy: Int = DEFAULT, message: String) {
            prepareLog1(LogLevel.WARN, tag, hierarchy + 1, message)
        }

        /**
         * Error
         */
        fun e(message: String) {
            e(getGlobalTag(), 1, message)
        }

        /**
         * Error
         */
        fun e(tag: String, message: String) {
            e(tag, 1, message)
        }

        /**
         * Error
         */
        fun e(hierarchy: Int, message: String) {
            e(getGlobalTag(), hierarchy + 1, message)
        }

        /**
         * Error
         */
        fun e(tag: String = getGlobalTag(), hierarchy: Int = DEFAULT, message: String) {
            prepareLog1(LogLevel.ERROR, tag, hierarchy + 1, message)
        }

        /**
         * Assert
         */
        fun wtf(message: String) {
            wtf(getGlobalTag(), 1, message)
        }

        /**
         * Assert
         */
        fun wtf(tag: String, message: String) {
            wtf(tag, 1, message)
        }

        /**
         * Assert
         */
        fun wtf(hierarchy: Int, message: String) {
            wtf(getGlobalTag(), hierarchy + 1, message)
        }

        /**
         * Assert
         */
        fun wtf(tag: String = getGlobalTag(), hierarchy: Int = DEFAULT, message: String) {
            prepareLog1(LogLevel.ASSERT, tag, hierarchy + 1, message)
        }

        private fun loggable(): Boolean {
            if (mEnableLogTree) {
                return mLogTree!!.isLoggable()
            } else if (isLoggable || mPropertyLoggable) {
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

        private fun prepareLog1(level: LogLevel, tag: String, hierarchy: Int, message: String) {
            if (loggable()) {
                val trace = Throwable().stackTrace[hierarchy + 1]
                val logPrefix = getPrefix(trace)

                if (message.length > mMaxLineLength) {
                    var isFirst = true
                    var remainMessage = message
                    while (remainMessage.length > mMaxLineLength) {
                        remainMessage.subSequence(0, mMaxLineLength).also {
                            if (isFirst) {
                                isFirst = false
                                prepareLog2(level, tag, "$logPrefix↘: $it")
                            } else {
                                prepareLog2(level, tag, "$logPrefix↗: $it")
                            }
                        }

                        remainMessage = remainMessage.substring(mMaxLineLength)
                    }

                    if (remainMessage.isNotEmpty()) {
                        prepareLog2(level, tag, "$logPrefix↗: $remainMessage")
                    }
                } else {
                    prepareLog2(level, tag, "$logPrefix: $message")
                }
            }
        }

        private fun prepareLog2(level: LogLevel, tag: String, message: String) {
            if (mEnableLogTree) {
                mLogTree?.log(level, tag, message)
            } else {
                prepareLog3(level, tag, message)
            }
        }

        private fun prepareLog3(level: LogLevel, tag: String, message: String) {
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
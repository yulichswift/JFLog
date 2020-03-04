package com.log

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

object JFLog {
    enum class LogLevel {
        VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
    }

    interface BaseTree {
        fun isLoggable(): Boolean
        fun log(level: LogLevel, tag: String, message: String)
    }

    abstract class LogTree : BaseTree {
        override fun log(level: LogLevel, tag: String, message: String) {
            prepareLog3(level, tag, message)
        }
    }

    private const val MAX_TAG_LENGTH = 23
    private const val DEFAULT = 1

    private var mMaxLineLength = 500
    private var mGlobalTag = "JFLog"
    private var mEnableLogTree = false
    private var mLogTree: LogTree? = null
    private val mPropertyLoggable by lazy { Log.isLoggable("jflog", Log.DEBUG) }

    @JvmField
    var isLoggable = true

    @JvmStatic
    fun setGlobalTag(tag: String) {
        if (tag.length > MAX_TAG_LENGTH) {
            mGlobalTag = tag.substring(0, MAX_TAG_LENGTH)
        } else {
            mGlobalTag = tag
        }
    }

    @JvmStatic
    fun getGlobalTag() = mGlobalTag

    @JvmStatic
    fun setGlobalMaxLineLength(length: Int) {
        mMaxLineLength = length
    }

    @JvmStatic
    fun setGlobalLogTree(logTree: LogTree?) {
        mEnableLogTree = logTree != null
        mLogTree = logTree
    }

    /**
     * Verbose
     */
    @JvmStatic
    fun v(message: String) =
        v(getGlobalTag(), message, 1)


    /**
     * Verbose
     */
    @JvmStatic
    fun v(tag: String, message: String) =
        v(tag, message, 1)

    /**
     * Verbose
     */
    @JvmStatic
    fun v(tag: String = getGlobalTag(), message: String, hierarchy: Int = DEFAULT) =
        prepareLog1(LogLevel.VERBOSE, tag, message, hierarchy + 1)

    /**
     * Debug
     */
    @JvmStatic
    fun d(message: String) =
        d(getGlobalTag(), message, 1)


    /**
     * Debug
     */
    @JvmStatic
    fun d(tag: String, message: String) =
        d(tag, message, 1)

    /**
     * Debug
     */
    @JvmStatic
    fun d(tag: String = getGlobalTag(), message: String, hierarchy: Int = DEFAULT) =
        prepareLog1(LogLevel.DEBUG, tag, message, hierarchy + 1)

    /**
     * Info
     */
    @JvmStatic
    fun i(message: String) =
        i(getGlobalTag(), message, 1)


    /**
     * Info
     */
    @JvmStatic
    fun i(tag: String, message: String) =
        i(tag, message, 1)

    /**
     * Info
     */
    @JvmStatic
    fun i(tag: String = getGlobalTag(), message: String, hierarchy: Int = DEFAULT) =
        prepareLog1(LogLevel.INFO, tag, message, hierarchy + 1)

    /**
     * Warn
     */
    @JvmStatic
    fun w(message: String) =
        w(getGlobalTag(), message, 1)

    /**
     * Warn
     */
    @JvmStatic
    fun w(tag: String, message: String) =
        w(tag, message, 1)

    /**
     * Warn
     */
    @JvmStatic
    fun w(tag: String = getGlobalTag(), message: String, hierarchy: Int = DEFAULT) =
        prepareLog1(LogLevel.WARN, tag, message, hierarchy + 1)

    /**
     * Error
     */
    @JvmStatic
    fun e(message: String?): String? {
        if (!message.isNullOrEmpty()) {
            return e(getGlobalTag(), message, 1)
        }

        return null
    }

    /**
     * Error
     */
    @JvmStatic
    fun e(tag: String, message: String?): String? {
        if (!message.isNullOrEmpty()) {
            return e(tag, message, 1)
        }

        return null
    }

    /**
     * Error
     */
    @JvmStatic
    fun e(tag: String = getGlobalTag(), message: String, hierarchy: Int = DEFAULT) =
        prepareLog1(LogLevel.ERROR, tag, message, hierarchy + 1)

    /**
     * Error
     */
    @JvmStatic
    fun e(t: Throwable) =
        e(getGlobalTag(), t, 1)

    /**
     * Error
     */
    @JvmStatic
    fun e(tag: String, t: Throwable) =
        e(tag, t, 1)


    /**
     * Error
     */
    @JvmStatic
    fun e(tag: String = getGlobalTag(), t: Throwable, hierarchy: Int = DEFAULT) =
        prepareLog1(LogLevel.ERROR, tag, getStackTrace(t), hierarchy + 1, false)

    /**
     * Assert
     */
    @JvmStatic
    fun wtf(message: String) =
        wtf(getGlobalTag(), message, 1)

    /**
     * Assert
     */
    @JvmStatic
    fun wtf(tag: String, message: String) =
        wtf(tag, message, 1)

    /**
     * Assert
     */
    @JvmStatic
    fun wtf(tag: String = getGlobalTag(), message: String, hierarchy: Int = DEFAULT) =
        prepareLog1(LogLevel.ASSERT, tag, message, hierarchy + 1)

    private fun getStackTrace(t: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
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

    private fun prepareLog1(level: LogLevel, tag: String, message: String, hierarchy: Int) =
        prepareLog1(level, tag, message, hierarchy + 1, true)

    private fun prepareLog1(
        level: LogLevel,
        tag: String,
        message: String,
        hierarchy: Int,
        needLimitLength: Boolean
    ): String? {
        if (loggable()) {
            val stackTrace = Throwable().stackTrace
            val trace =
                if (stackTrace.size > hierarchy + 1) {
                    stackTrace[hierarchy + 1]
                } else {
                    stackTrace[stackTrace.size - 1]
                }

            val logPrefix = getPrefix(trace)
            val output = "$logPrefix: $message"

            if (needLimitLength && message.length > mMaxLineLength) {
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
                prepareLog2(level, tag, output)
            }

            return output
        }

        return null
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
package com.jeff.jflogapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.log.JFLog
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setGlobalLogTree is not necessary
        JFLog.setGlobalLogTree(AppLogTree())

        JFLog.v("Init")
        JFLog.d("Init")
        JFLog.i("Init")
        JFLog.w("Init")
        JFLog.e("Init")
        JFLog.wtf("Init")

        try {
            val test: String? = null
            test!!.toBigDecimal()
        } catch (e: Exception) {
            JFLog.e(e)
        }
    }

    fun onClick1(view: View) {
        JFLog.d("Click1")
        JFLog.d("App", "Click1")
        JFLog.d(message = "Click1", hierarchy = 0)
        JFLog.d("App", "Click1", 0)
    }
}

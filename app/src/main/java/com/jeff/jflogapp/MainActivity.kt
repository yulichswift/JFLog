package com.jeff.jflogapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.log.JFLog

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
    }

    fun onClick1(view: View) {
        JFLog.d("Click1")
        JFLog.d("App", "Click1")
        JFLog.d(0, "Click1")
        JFLog.d("App", 0, "Click1")
    }
}

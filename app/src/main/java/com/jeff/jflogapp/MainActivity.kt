package com.jeff.jflogapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.log.JFLog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setLogTree is not necessary
        JFLog.setLogTree(AppLogTree())

        JFLog.v("Init")
        JFLog.d("Init")
        JFLog.i("Init")
        JFLog.w("Init")
        JFLog.e("Init")
        JFLog.wtf("Init")
    }

    fun onClick1(view: View) {
        JFLog.d("Clicked 1")
    }
}

package com.jeff.jflogapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.log.JFLog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JFLog.setLogTree(AppLogTree())
        JFLog.d("Init")
    }

    fun onClick1(view: View) {
        JFLog.d("Clicked 1")
    }
}

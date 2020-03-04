package com.jeff.jflogapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jeff.jflogapp.databinding.ActivityMainBinding
import com.log.JFLog

class MainActivity : AppCompatActivity() {

    private val stringBuilder = StringBuilder()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setGlobalLogTree is not necessary
        JFLog.setGlobalLogTree(AppLogTree())

        JFLog.v("Init").also {
            stringBuilder.appendln(it)
        }
        JFLog.d("Init").also {
            stringBuilder.appendln(it)
        }
        JFLog.i("Init").also {
            stringBuilder.appendln(it)
        }
        JFLog.w("Init").also {
            stringBuilder.appendln(it)
        }
        JFLog.e("Init").also {
            stringBuilder.appendln(it)
        }
        JFLog.wtf("Init").also {
            stringBuilder.appendln(it)
        }

        try {
            val test: String? = null
            test!!.toBigDecimal()
        } catch (e: Exception) {
            JFLog.e(e).also {
                stringBuilder.appendln(it)
            }
        }

        binding.textLog.text = stringBuilder.toString()
    }

    fun onClick1(view: View) {
        JFLog.d("Click1").also {
            stringBuilder.appendln(it)
        }
        JFLog.d("App", "Click1").also {
            stringBuilder.appendln(it)
        }
        JFLog.d(message = "Click1", hierarchy = 0).also {
            stringBuilder.appendln(it)
        }
        JFLog.d("App", "Click1", 0).also {
            stringBuilder.appendln(it)
        }

        binding.textLog.text = stringBuilder.toString()
    }
}

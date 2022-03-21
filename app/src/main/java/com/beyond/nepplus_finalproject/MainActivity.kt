package com.beyond.nepplus_finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun SetupEvents() {
        TODO("Not yet implemented")
    }

    override fun setValues() {
        TODO("Not yet implemented")
    }
}
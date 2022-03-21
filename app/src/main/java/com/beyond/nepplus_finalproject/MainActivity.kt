package com.beyond.nepplus_finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.databinding.ActivityMainBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setValues()
        SetupEvents()

    }

    override fun SetupEvents() {
    }

    override fun setValues() {
    }
}
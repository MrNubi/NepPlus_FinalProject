package com.beyond.nepplus_finalproject.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setValues()
        SetupEvents()
    }

    override fun SetupEvents() {
    }

    override fun setValues() {
    }
}
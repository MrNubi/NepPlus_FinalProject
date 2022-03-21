package com.beyond.nepplus_finalproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.Auth.LogInActivity
import com.beyond.nepplus_finalproject.databinding.ActivitySplashBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : BaseActivity() {

    private lateinit var binding : ActivitySplashBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)



        setValues()
        SetupEvents()

    }

    override fun SetupEvents() {
        auth = Firebase.auth

        if(auth.currentUser?.uid == null) {
            Log.d("SplashActivity", "null")

            Handler().postDelayed({
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }, 3000)

        } else {
            Log.d("SplashActivity", "not null")

                val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_current_user_login, null)
                val mBuilder = AlertDialog.Builder(mContext)
                    .setView(mDialogView)
                    .setTitle("기존에 로그인하던 아이디가 있습니다.")

                val alertDialog = mBuilder.show()
                alertDialog.findViewById<Button>(R.id.btn_yes)?.setOnClickListener {
                    Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }

                alertDialog.findViewById<Button>(R.id.btn_no)?.setOnClickListener {

                    auth.signOut()
                    startActivity(Intent(this, LogInActivity::class.java))
                    finish()

                }



        }

    }

    override fun setValues() {
        auth = Firebase.auth



    }
}
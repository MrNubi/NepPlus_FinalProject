package com.beyond.nepplus_finalproject.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.beyond.nepplus_finalproject.MainActivity
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.databinding.ActivityLogInBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        setValues()
        SetupEvents()
    }

    override fun SetupEvents() {

        binding.btnLogIn.setOnClickListener {

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPw.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        Log.d("로그인", "성공")

                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()

                    } else {
                        Log.d("로그인", "실패")
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show()

                    }
                }


        }
        binding.btnMoveToSignIn.setOnClickListener {
            val intent = Intent(mContext, SignUpActivity::class.java)

            startActivity(intent)
        }

    }

    override fun setValues() {



    }
}
package com.beyond.nepplus_finalproject.Auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.MainActivity
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.databinding.ActivityLogInBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding =  DataBindingUtil.setContentView(this,R.layout.activity_log_in)
        setValues()
        SetupEvents()
    }

    override fun SetupEvents() {

        binding.btnLogIn.setOnClickListener {


            LoginProcess()

        }
        binding.btnMoveToSignIn.setOnClickListener {
            val intent = Intent(mContext, SignUpActivity::class.java)

            startActivity(intent)
        }
        binding.btnFindingPw.setOnClickListener {
            //auth.sendPasswordResetEmail()
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_find_pw, null)
            val mBuilder = AlertDialog.Builder(mContext)
                .setView(mDialogView)
                .setTitle("기존에 로그인하던 아이디가 있습니다.")

            val alertDialog = mBuilder.show()
            alertDialog.findViewById<Button>(R.id.btn_findingPw)?.setOnClickListener {



//                Toast.makeText(this, "로그인에 성공했습니다", Toast.LENGTH_LONG).show()

//                startActivity(Intent(this, MainActivity::class.java))
//                finish()

            }


        }



    }
    private fun LoginProcess(){
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPw.text.toString()
        auth = Firebase.auth


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

    override fun setValues() {



    }
}
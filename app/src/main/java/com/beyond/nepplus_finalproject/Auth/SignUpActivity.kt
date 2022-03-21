package com.beyond.nepplus_finalproject.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.MainActivity
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.databinding.ActivitySignInBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : BaseActivity() {
  private lateinit var binding : ActivitySignInBinding
  private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in)

        setValues()
        SetupEvents()
    }



    override fun setValues() {
    }
    private fun SignUpCheck() : Boolean {
        var isGoToJoin = true

        val edt_email = binding.edtEmailArea.text.toString()
        val edt_password1 = binding.edtPasswordArea1.text.toString()
        val edt_password2 = binding.edtPasswordArea2.text.toString()

        // 각각의 값이 비어있는지 확인1

        if (edt_email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }
        // 각각의 값이 비어있는지 확인2
        if (edt_password1.isEmpty()) {
            Toast.makeText(this, "비밀 번호를 입력해주세요", Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }
        // 각각의 값이 비어있는지 확인3
        if (edt_password2.isEmpty()) {
            Toast.makeText(this, "비밀번호 확인란을 입력해주세요", Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        // 비밀번호 2개가 같은지 확인
        if (!edt_password1.equals(edt_password2)) {
            Toast.makeText(this, "비밀번호가 서로 다릅니다", Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        // 비밀번호가 6자 이상인지
        if (edt_password1.length < 6) {
            Toast.makeText(this, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_LONG).show()
            isGoToJoin = false
        }

        return isGoToJoin

    }//fun SignUp()

    private fun Signup(isGoToJoin : Boolean){

        auth = Firebase.auth

        if (isGoToJoin == true) {
//            val retrfit = ServerAPI.getRetrofit(mContext)
//          var  apiList : APIList = retrfit.create(APIList::class.java)


//            val edt_userName = binding.edtUsername.text.toString()
            val edt_email = binding.edtEmailArea.text.toString()
            val edt_password1 = binding.edtPasswordArea1.text.toString()

            auth.createUserWithEmailAndPassword(edt_email, edt_password1).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)


                } else {
                    Log.d("실패","왜?")
                }
            }


        }
    }//private fun Signup(isGoToJoin : Boolean)

    override fun SetupEvents() {

        binding.btnSignIn.setOnClickListener {
            val checkSign = SignUpCheck()


                Signup(checkSign)


        }


    }

}
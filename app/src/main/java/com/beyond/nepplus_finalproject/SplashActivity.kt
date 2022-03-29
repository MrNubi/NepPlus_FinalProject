package com.beyond.nepplus_finalproject

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.Auth.LogInActivity
import com.beyond.nepplus_finalproject.databinding.ActivitySplashBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class SplashActivity : BaseActivity() {

    private lateinit var binding : ActivitySplashBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        setValues()

        val pl = object : PermissionListener{
            override fun onPermissionGranted() {
                Log.d("권한 허용","향")
                SetupEvents()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(mContext, "권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()


            }
        }

        TedPermission.create()
            .setPermissionListener(pl)
            .setDeniedMessage("권한을 거부하면 통화가 불가능합니다. 설정 > 권한에서 허용해주세요.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정으로 가기")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET)
            .check()




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
                    Toast.makeText(this, "로그인에 성공했습니다", Toast.LENGTH_LONG).show()

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




        }





}
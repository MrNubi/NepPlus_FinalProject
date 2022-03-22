package com.beyond.nepplus_finalproject.inheritClass

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        mContext = this

//        val retrfit = ServerAPI.getRetrofit(mContext)
//        apiList = retrfit.create(APIList::class.java)
    }

    abstract fun SetupEvents()
    abstract fun setValues()
    var mBackWait: Long = 0

//    override fun onBackPressed() {
//        // 뒤로가기버튼클릭
//
//        if (System.currentTimeMillis() - mBackWait >= 2000) {
//            mBackWait = System.currentTimeMillis()
//            Toast.makeText(baseContext, "한번더누르시면종료됩니다", Toast.LENGTH_SHORT).show()
//        } else {moveTaskToBack(true);
//            finishAndRemoveTask();
//            android.os.Process.killProcess(android.os.Process.myPid());}}
    }

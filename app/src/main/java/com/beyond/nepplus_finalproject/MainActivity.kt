package com.beyond.nepplus_finalproject

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.Board.BoardActivity
import com.beyond.nepplus_finalproject.Board.BoardinnerActivity
import com.beyond.nepplus_finalproject.adapter.MainLVadapter
import com.beyond.nepplus_finalproject.data.LvModel
import com.beyond.nepplus_finalproject.databinding.ActivityMainBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.beyond.nepplus_finalproject.util.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity() {
    lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityMainBinding
    private var database = Firebase.database
    private val LvDataList = mutableListOf<LvModel>()
    private val LvKeyList = mutableListOf<String>()
    private lateinit var LVadapter : MainLVadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setValues()
        SetupEvents()
        binding.btnSetting.setOnClickListener {
            val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_current_user_login, null)
            val mBuilder = AlertDialog.Builder(mContext)
                .setView(mDialogView)
                .setTitle("Logout 하시겠습니까")


            val alertDialog = mBuilder.show()
            val k = alertDialog.findViewById<TextView>(R.id.txt_cuText)
            k?.text = "로그아웃 하세겠습니까"
            alertDialog.findViewById<Button>(R.id.btn_yes)?.setOnClickListener {
                auth = Firebase.auth
                auth.signOut()

                Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show()

                val intent = Intent(this, SplashActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            alertDialog.findViewById<Button>(R.id.btn_no)?.setOnClickListener {
                alertDialog.dismiss()
            }

        }

    }
    override fun setValues() {
        LVadapter = MainLVadapter(LvDataList)

        binding.mainLV.adapter = LVadapter

        getFBBoardData()
    }

    override fun SetupEvents() {

        binding.mainLV.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BoardinnerActivity::class.java)
            intent.putExtra("key", LvKeyList[position])
            Log.d("슈팅스타", LvKeyList[position])
            startActivity(intent)
        }

        binding.btnWrite.setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)
        }

        binding.btnSetting.setOnClickListener {

        }


    }
    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                LvDataList.clear()
                // Get Post object and use the values to update the UI
                // val post = dataSnapshot.getValue<Post>()
                // ...
                for (dataModel in dataSnapshot.children){

//                    dataModel.key

                    Log.d(ContentValues.TAG, dataModel.toString())

                    val item = dataModel.getValue(LvModel::class.java)
                    LvDataList.add(item!!)
                    LvKeyList.add(dataModel.key.toString())




                }
                LvKeyList.reverse()
                LvDataList.reverse()

                LVadapter.notifyDataSetChanged()
                Log.d("곽", LvDataList.toString())



            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "갸갸거겨", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }

    override fun onBackPressed() {
        // 뒤로가기버튼클릭

        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(baseContext, "한번더누르시면종료됩니다", Toast.LENGTH_SHORT).show()
        } else {moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());}}


}
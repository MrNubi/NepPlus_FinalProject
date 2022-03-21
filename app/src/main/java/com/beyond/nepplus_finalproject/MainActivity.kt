package com.beyond.nepplus_finalproject

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.Board.BoardActivity
import com.beyond.nepplus_finalproject.Board.BoardinnerActivity
import com.beyond.nepplus_finalproject.adapter.MainLVadapter
import com.beyond.nepplus_finalproject.data.LvModel
import com.beyond.nepplus_finalproject.databinding.ActivityMainBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.beyond.nepplus_finalproject.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity() {
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


}
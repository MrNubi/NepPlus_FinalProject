package com.beyond.nepplus_finalproject.Board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.adapter.CommentLVAdapter
import com.beyond.nepplus_finalproject.adapter.ReReplyAreaAdapter
import com.beyond.nepplus_finalproject.data.BoardModel
import com.beyond.nepplus_finalproject.data.CommentModel
import com.beyond.nepplus_finalproject.data.ReReplyModel
import com.beyond.nepplus_finalproject.databinding.ActivityBoardReReplyBinding
import com.beyond.nepplus_finalproject.databinding.ActivityCommentMoreBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.beyond.nepplus_finalproject.util.FBA
import com.beyond.nepplus_finalproject.util.FBRef
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class CommentMoreActivity: BaseActivity() {

    private lateinit var binding: ActivityCommentMoreBinding
    private lateinit var title :String
    private lateinit var time :String
    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    private lateinit var key:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_re_reply)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_more)

        setValues()
        SetupEvents()
        key = intent.getStringExtra("key").toString()

        getCommentData(key)





    }

    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                commentAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)


    }

    fun insertComment(key : String){

        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.reCommentArea.text.toString(),
                    FBA.getTime(),
                    key
                )
            )

        Toast.makeText(mContext, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.reCommentArea.setText("")

    }

    private fun showDialog(){

        val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(mContext)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(mContext, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()

            val intent = Intent(mContext, BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(mContext, "삭제완료", Toast.LENGTH_LONG).show()
            finish()

        }



    }



    override fun setValues() {
        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter
    }

    override fun SetupEvents() {
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

//        binding.commentLV.setOnItemClickListener { parent, view, position, id ->
//            val intent = Intent(mContext, Board_Re_Reply_Activity::class.java)
//            intent.putExtra("Commenttitle", commentDataList[position].commentTitle.toString())
//            intent.putExtra("Commenttime", commentDataList[position].commentCreatedTime.toString())
//            intent.putExtra("key",key)
//            Log.d("ToReRepltBoard", commentDataList[position].toString())
//            startActivity(intent)
//        }

    }


}
package com.beyond.nepplus_finalproject.Board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.beyond.nepplus_finalproject.data.BoardModel
import com.beyond.nepplus_finalproject.data.CommentModel
import com.beyond.nepplus_finalproject.databinding.ActivityBoardinnerBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.beyond.nepplus_finalproject.util.FBA
import com.beyond.nepplus_finalproject.util.FBRef
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardinnerActivity : BaseActivity() {

    private val TAG = BoardinnerActivity::class.java.simpleName



    private lateinit var key:String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    private lateinit var binding: ActivityBoardinnerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this, R.layout.activity_boardinner)
        setValues()
        SetupEvents()
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)
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
                commentDataList.reverse()
                commentAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
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
                    binding.commentArea.text.toString(),
                    FBA.getTime(),
                    key
                )
            )

        Toast.makeText(mContext, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")

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

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Glide.with(mContext)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else {

                binding.getImageArea.isVisible = false
            }
        })


    }


    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {

                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    val myUid = FBA.getUid()
                    val writerUid = dataModel.uid

                    if(myUid.equals(writerUid)){
                        Log.d(TAG, "내가 쓴 글")
                        binding.boardSettingIcon.isVisible = true
                    } else {
                        Log.d(TAG, "내가 쓴 글 아님")
                    }

                } catch (e : Exception){

                    Log.d(TAG, "삭제완료")

                }



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)



    }
    override fun setValues() {
        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter
    }

    override fun SetupEvents() {
        binding.getImageArea.setOnClickListener {

            binding.getImageArea.isVisible = false
            binding.txtReturnImg.isVisible =true


        }
        binding.txtReturnImg.setOnClickListener {
            binding.getImageArea.isVisible = true
            binding.txtReturnImg.isVisible =false
        }
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }
        binding.commentBtn.setOnClickListener {
            insertComment(key)

        }
        binding.btnBoardInnerMore.setOnClickListener {
            val intent = Intent(mContext, CommentMoreActivity::class.java)

            intent.putExtra("key",key)

            startActivity(intent)
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
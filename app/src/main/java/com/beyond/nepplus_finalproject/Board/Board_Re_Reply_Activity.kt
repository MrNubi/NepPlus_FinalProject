package com.beyond.nepplus_finalproject.Board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.adapter.CommentLVAdapter
import com.beyond.nepplus_finalproject.data.CommentModel
import com.beyond.nepplus_finalproject.databinding.ActivityBoardReReplyBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.beyond.nepplus_finalproject.util.FBA
import com.beyond.nepplus_finalproject.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Board_Re_Reply_Activity : BaseActivity() {

    private lateinit var binding: ActivityBoardReReplyBinding
    private lateinit var title :String
    private lateinit var time :String
    private lateinit var re_CommentLVAdapter: CommentLVAdapter
    private val re_commentDataList = mutableListOf<CommentModel>()

    private lateinit var key:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_re_reply)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_re_reply)

        title = intent.getStringExtra("Commenttitle").toString()
        time = intent.getStringExtra("Commenttime").toString()
        key =  intent.getStringExtra("key").toString()

        // 데이터 받아옴
        Log.d("뭐여", title)
        Log.d("뭐여2", time)

        //키는 여차하면 getUid하면 되니까, 굳이 로그 안찍어도 될듯.
        var time2 = time.split(".").toString()
        var time3 = time.split(".")[0]
        var time4 = time.split(".")[1]
        var time5 = time.split(".")[2]
        var time6 = time5.split(";")[0]
        var time7 = time5.split(";")[1].split(":")[0]
        var time8 = time5.split(";")[1].split(":")[1]


        var TT = "$time3$time4$time5$time6$time7$time8"
        // 시간값을 토큰값으로 쓰려는데, ':'이랑';'가 들어가서 안 되길래 그걸 빼기 위한 발악

        Log.d("뭐여4", time2)
        Log.d("뭐여5", TT.toString())
        getCommentData(key, TT)
        // 발악2. 다행히 잘 찍혔음





        binding.ReReplyTitle.text = title!!
        binding.REReplyTime.text = time!!

        //이름이랑 시간을 받아온 값으로 초기화

        binding.reCommentBtn.setOnClickListener {
            insertComment(key, TT)
        }
        //대댓글 입력 버튼

        re_CommentLVAdapter = CommentLVAdapter(re_commentDataList)
        binding.reCommentLV.adapter = re_CommentLVAdapter


    }



    override fun SetupEvents() {
        TODO("Not yet implemented")
    }

    override fun setValues() {
        TODO("Not yet implemented")
    }
    fun getCommentData(key : String, Time : String){
        //Firebase에서 데이터 긁어오기


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                re_commentDataList.clear()

                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    re_commentDataList.add(item!!)

                }

                re_CommentLVAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("캬옹", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.re_commentRef.child(key).child(Time).addValueEventListener(postListener)


    }

    fun insertComment(key : String, TT : String){

        FBRef.re_commentRef
            .child(key)
            .child(TT)
            .push()
            .setValue(
                CommentModel(
                    binding.reCommentArea.text.toString(),
                    FBA.getTime()
                )
            )

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.reCommentArea.setText("")

    }
}
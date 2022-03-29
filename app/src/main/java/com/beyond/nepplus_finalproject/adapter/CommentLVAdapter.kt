package com.beyond.nepplus_finalproject.adapter

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import com.beyond.nepplus_finalproject.Board.Board_Re_Reply_Activity
import com.beyond.nepplus_finalproject.Board.BoardinnerActivity
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.data.CommentModel
import com.beyond.nepplus_finalproject.data.ReReplyModel
import com.beyond.nepplus_finalproject.util.FBA
import com.beyond.nepplus_finalproject.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gun0912.tedpermission.provider.TedPermissionProvider.context


class CommentLVAdapter(val commentList : MutableList<CommentModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item, parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.titleArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        val btnShow = view?.findViewById<Button>(R.id.btn_reReplyShow)
        val btnreReply = view?.findViewById<ImageView>(R.id.btn_reReplyPush)
        val LVlayout = view?.findViewById<LinearLayout>(R.id.linlay_LVcover)
        val LVReReply = view?.findViewById<ListView>(R.id.LV_re_comment)

        title!!.text = commentList[position].commentTitle
        title!!.maxLines = 1
        time!!.text = commentList[position].commentCreatedTime
        btnShow?.setOnClickListener {
            var k = LVlayout?.isVisible

            if (k == true) {
                LVlayout?.isVisible = false
                btnShow.text = "대댓글"
            } else {
                title!!.maxLines = 100
                var rereDataList = mutableListOf<ReReplyModel>()
                var rereAdapter = ReReplyAdapter(rereDataList)
                LVReReply?.adapter = rereAdapter
                LVReReply?.setOnItemClickListener { adapterView, view, i, l ->
                    val mintent = Intent(context, Board_Re_Reply_Activity::class.java)
            mintent.putExtra("Commenttitle", commentList[position].commentTitle.toString())
            mintent.putExtra("Commenttime", commentList[position].commentCreatedTime.toString())
            mintent.putExtra("key",commentList[position].key)
            Log.d("ToReRepltBoard", commentList[position].toString())
            context.startActivity(mintent.addFlags(FLAG_ACTIVITY_NEW_TASK))



                }


                val ttime = commentList[position].commentCreatedTime
                var ttime2 = ttime.split(".").toString()
                var ttime3 = ttime.split(".")[0]
                var ttime4 = ttime.split(".")[1]
                var ttime5 = ttime.split(".")[2]
                var ttime6 = ttime5.split(";")[0]
                var ttime7 = ttime5.split(";")[1].split(":")[0]
                var ttime8 = ttime5.split(";")[1].split(":")[1]


                var TT = "$ttime3$ttime4$ttime5$ttime6$ttime7$ttime8"
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        rereDataList.clear()

                        for (dataModel in dataSnapshot.children) {

                            val item = dataModel.getValue(ReReplyModel::class.java)
                            rereDataList.add(item!!)

                        }
                        rereDataList.reverse()
                        rereAdapter.notifyDataSetChanged()


                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("캬옹", "loadPost:onCancelled", databaseError.toException())
                    }
                }
                FBRef.re_commentRef.child(commentList[position].key).child(TT).addValueEventListener(postListener)
                LVlayout?.isVisible = true
                btnShow.text = "댓글 접기"


            }//else

        }//btnClickListener

        btnreReply?.setOnClickListener {
            var edt_rereply = view?.findViewById<EditText>(R.id.edt_re_replyText)
            var textReReply = edt_rereply?.text.toString()

            val ttime = commentList[position].commentCreatedTime
            var ttime2 = ttime.split(".").toString()
            var ttime3 = ttime.split(".")[0]
            var ttime4 = ttime.split(".")[1]
            var ttime5 = ttime.split(".")[2]
            var ttime6 = ttime5.split(";")[0]
            var ttime7 = ttime5.split(";")[1].split(":")[0]
            var ttime8 = ttime5.split(";")[1].split(":")[1]


            var TT = "$ttime3$ttime4$ttime5$ttime6$ttime7$ttime8"

            FBRef.re_commentRef
                .child(commentList[position].key)
                .child(TT)
                .push()
                .setValue(
                    CommentModel(
                        textReReply,
                        FBA.getTime()
                    )
                )

            Toast.makeText(context, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
            edt_rereply?.setText("")

        }

        return view!!
    }


}
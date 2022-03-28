package com.beyond.nepplus_finalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.data.CommentModel
import com.beyond.nepplus_finalproject.data.ReReplyModel

class ReReplyAreaAdapter(val reReplyList : MutableList<ReReplyModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return reReplyList.size
    }

    override fun getItem(p0: Int): Any {
        return reReplyList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1

        if (view == null) {
            view = LayoutInflater.from(p2?.context).inflate(R.layout.item_re_reply, p2, false)
        }

        val title = view?.findViewById<TextView>(R.id.text_re_reply_title)
        val time = view?.findViewById<TextView>(R.id.text_re_reply_time)



        title!!.text = reReplyList[p0].commentTitle
        time!!.text =  reReplyList[p0].commentCreatedTime



        return view!!
    }

}
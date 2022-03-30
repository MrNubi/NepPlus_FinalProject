package com.beyond.nepplus_finalproject.Board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyond.nepplus_finalproject.R
import com.beyond.nepplus_finalproject.data.LvModel
import com.beyond.nepplus_finalproject.databinding.ActivityBoardBinding
import com.beyond.nepplus_finalproject.inheritClass.BaseActivity
import com.beyond.nepplus_finalproject.util.FBA
import com.beyond.nepplus_finalproject.util.FBRef
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardActivity : BaseActivity() {

    private lateinit var binding : ActivityBoardBinding

    private var imgClicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board)
        setValues()
        SetupEvents()


    }
    override fun SetupEvents() {
        binding.btnBoardwrite.setOnClickListener{
            val title = binding.BETTitle1.text.toString()
            val content = binding.BETContents1.text.toString()
            val uid = FBA.getUid()
            val time = FBA.getTime()

            val key = FBRef.boardRef.push().key.toString()
            val A = FBRef.storageRef.child("${key}.png").downloadUrl
            FBRef.boardRef
                .child(key)
                .setValue(LvModel(title, content, uid, time,"1",key))
            if(imgClicked==true){imageUPload(key)}
            Toast.makeText(mContext, "게시글이 작성되었습니다", Toast.LENGTH_LONG).show()
            finish()
        }

        binding.BEI1.setOnClickListener {

            val key = FBRef.boardRef.push().key.toString()
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            imgClicked = true
        }
    }

    override fun setValues() {
    }
    private fun imageUPload(key: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key +".png")
        val mountainsRef2 = Firebase.storage.reference.child("main").child(key)
        val imageView = binding.BEI1
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = mountainsRef.putBytes(data)//<- 경로설정
        uploadTask.addOnFailureListener {
        }.addOnSuccessListener { taskSnapshot ->
        }
        var uploadTask2 = mountainsRef2.putBytes(data)//<- 경로설정
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 100){
            val BEI1 = findViewById<ImageView>(R.id.BEI1)
            Glide.with(mContext).load(data?.data).into(BEI1)

        }

    }




}

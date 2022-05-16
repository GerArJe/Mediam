package com.example.mediam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mediam.databinding.ActivityPostDetailBinding


class PostDetailActivity : AppCompatActivity() {
    lateinit var _binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title :String? = intent.getStringExtra("title")
        val img: String? = intent.getStringExtra("img")
        val desc:String?=intent.getStringExtra("desc")
        val topic:String?=intent.getStringExtra("topic")



        _binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)

        title.let {
            _binding.titlePostDetail.text = it
        }

        desc.let {
            _binding.descPostDetail.text = it
        }

        topic.let {
            _binding.topicPostDetail.text = it
        }


    }
}


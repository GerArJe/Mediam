package com.example.mediam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.databinding.ActivityPostDetailBinding
import com.example.mediam.model.entity.Post
import com.example.mediam.post.view.PostActivity


class PostDetailActivity : AppCompatActivity() {
    lateinit var _binding: ActivityPostDetailBinding
    lateinit var viewModel: PostDetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id:String?=intent.getStringExtra("id")




        _binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
        viewModel = ViewModelProvider(this)[PostDetailActivityViewModel::class.java]

        id?.let {
            viewModel.getPostById(it)
        }

        _binding.post = Post()

        viewModel.post.observe(this){
            it?.let {
                _binding.post = it
            }
        }

        _binding.btnEditPostDetail.setOnClickListener {
            /*val intentForm = Intent(applicationContext, ::class.java)
            intentForm.putExtra("post", _binding.post)
            startActivity(intentForm)

            Enviando objeto del post para actualizar

            */

        }

        _binding.btnReturnPostDetail.setOnClickListener {
            finish()
        }
    }
}


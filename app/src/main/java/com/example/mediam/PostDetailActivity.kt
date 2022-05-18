package com.example.mediam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.databinding.ActivityPostDetailBinding
import com.example.mediam.model.entity.Post
import com.example.mediam.post.view.PostActivity


class PostDetailActivity : AppCompatActivity() {
    lateinit var _binding: ActivityPostDetailBinding
    lateinit var viewModel: PostDetailActivityViewModel
    private var idUser: String = ""
    var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra("id")




        _binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
        viewModel = ViewModelProvider(this)[PostDetailActivityViewModel::class.java]

        _binding.btnEditPostDetail.isVisible = false

        getUser()

        id?.let {
            viewModel.getPostById(it)
        }

        _binding.post = Post()

        viewModel.post.observe(this) {
            it?.let {
                _binding.post = it

                if (idUser !== "") {
                    _binding.btnEditPostDetail.isVisible = true
                    edit()
                }
            }
        }



        _binding.btnReturnPostDetail.setOnClickListener {
            finish()
        }
    }

    private fun edit() {
        _binding.btnEditPostDetail.setOnClickListener {
            val intentForm = Intent(applicationContext, PostActivity::class.java)
            intentForm.putExtra("post", viewModel.post.value)
            startActivity(intentForm)
        }
    }

    private fun getUser() {
        val preferences =
            getSharedPreferences("shad.pref", MODE_PRIVATE)
        idUser = preferences.getString("idUser", "").toString()
    }

    override fun onResume() {
        id?.let {
            viewModel.getPostById(it)
        }
        super.onResume()
    }
}


package com.example.mediam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mediam.model.entity.Post
import com.example.mediam.model.repository.PostRepository

class PostDetailActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val postRepository: PostRepository = PostRepository(application)
    lateinit var post: LiveData<Post>

    fun getPostById(myPostId: String) {
        postRepository.getById(myPostId)
        post = postRepository.postObserver
    }

}
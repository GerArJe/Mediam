package com.example.mediam.home.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mediam.model.entity.Post
import com.example.mediam.model.repository.PostRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val postRepository: PostRepository = PostRepository(application)
    var posts: LiveData<List<Post>> = postRepository.postsObserver

    fun loadAllPosts() {
        postRepository.loadPostsFirestore()
    }
}
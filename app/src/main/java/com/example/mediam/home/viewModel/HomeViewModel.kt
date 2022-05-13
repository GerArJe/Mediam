package com.example.mediam.home.viewModel

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mediam.model.entity.Filter
import com.example.mediam.model.entity.Post
import com.example.mediam.model.entity.Topic
import com.example.mediam.model.repository.PostRepository
import com.example.mediam.model.repository.TopicRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val postRepository: PostRepository = PostRepository(application)
    var posts: LiveData<List<Post>> = postRepository.postsObserver
    var filter: Filter = Filter()



    fun loadAllPosts() {
        postRepository.loadPostsFirestore()
    }



    fun loadPostByFilters() {
        postRepository.loadPostByFilters(filter)
    }


}
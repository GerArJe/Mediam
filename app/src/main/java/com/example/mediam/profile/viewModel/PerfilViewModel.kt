package com.example.mediam.profile.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mediam.model.entity.Post
import com.example.mediam.model.repository.PostRepository

class PerfilViewModel (application: Application): AndroidViewModel(application){

    private val postRepository:PostRepository = PostRepository(application)
    var posts: LiveData<List<Post>> = postRepository.postsObserver

    /*fun deleteProduct(myPost: Post){
        postRepository.deleteFirestore(myPost)
    }*/

    fun loadProducts(id:String) {
        postRepository.loadPostsFirestore(id)
    }

}
package com.example.mediam.post.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mediam.model.entity.Post
import com.example.mediam.model.entity.Topic
import com.example.mediam.model.repository.PostRepository
import com.example.mediam.model.repository.TopicRepository

class PostActivityViewModel (application: Application): AndroidViewModel(application) {
    var post: Post = Post()
    private val postRepository: PostRepository = PostRepository(application)
    private val topicRepository: TopicRepository = TopicRepository()
    var topics: LiveData<List<Topic>> = topicRepository.topicsObserver

    fun add(photoUri: Uri?): LiveData<String> {
        return postRepository.add(post, photoUri)
    }

    fun update(photoUri: Uri?): LiveData<Boolean> {
        return postRepository.update(post, photoUri)
    }

    fun getTopics() {
        topicRepository.get();
    }

    fun idPostComplete(post: Post): Boolean {
        return post.isComplete()
    }

}
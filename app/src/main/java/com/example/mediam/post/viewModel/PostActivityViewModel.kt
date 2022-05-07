package com.example.mediam.post.viewModel

import androidx.lifecycle.ViewModel
import com.example.mediam.model.entity.Post
import com.example.mediam.model.entity.Topic
import com.example.mediam.model.repository.PostRepository
import com.example.mediam.model.repository.TopicRepository

class PostActivityViewModel: ViewModel() {
    var post: Post = Post()
    var topics: Topic = Topic()
    private val postRepository: PostRepository = PostRepository()
    private val topicRepository: TopicRepository = TopicRepository()

    fun add() {}

    fun edit() {}

    fun getTopics() {
        topics
    }

}
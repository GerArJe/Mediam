package com.example.mediam.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mediam.model.entity.Filter
import com.example.mediam.model.entity.Topic
import com.example.mediam.model.repository.TopicRepository

class SearchActivityViewModel: ViewModel() {
    var filter: Filter = Filter()
    private val topicRepository: TopicRepository = TopicRepository()
    var topics: LiveData<List<Topic>> = topicRepository.topicsObserver

    fun getTopics() {
        topicRepository.get();
    }
}
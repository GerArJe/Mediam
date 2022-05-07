package com.example.mediam.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Publicar"
    }
    val text: LiveData<String> = _text
}
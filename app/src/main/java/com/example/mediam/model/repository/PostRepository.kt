package com.example.mediam.model.repository

import androidx.lifecycle.MutableLiveData
import com.example.mediam.model.entity.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostRepository {
    private val POSTS_COLLECTION: String = "posts"
    var postsObserver: MutableLiveData<List<Post>> = MutableLiveData()
    var postObserver: MutableLiveData<Post> = MutableLiveData()
    private val firestore: FirebaseFirestore = Firebase.firestore


}
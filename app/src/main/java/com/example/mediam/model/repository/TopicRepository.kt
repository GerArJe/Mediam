package com.example.mediam.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediam.model.entity.Topic
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TopicRepository {
    private val TOPICS_COLLECTION: String = "topics"
    var topicsObserver: MutableLiveData<List<Topic>> = MutableLiveData()
    private val firestore: FirebaseFirestore = Firebase.firestore

    fun get(): LiveData<List<Topic>> {
        firestore.collection(TOPICS_COLLECTION).get().addOnSuccessListener { response ->
            val topics: ArrayList<Topic> = arrayListOf<Topic>()
            if (!response.isEmpty) {
                for (document in response.documents) {
                    val post: Topic? = document.toObject(Topic::class.java)
                    post?.let {
                        it.id = document.id
                        topics.add(it)
                    }
                }
            }
            topicsObserver.value = topics
        }.addOnFailureListener {
            error("TopicRepository - get: ${it.message}")
        }
        return topicsObserver
    }
}
package com.example.mediam.model.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediam.model.entity.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class PostRepository {
    private val POSTS_COLLECTION: String = "posts"
    var postsObserver: MutableLiveData<List<Post>> = MutableLiveData()
    var postObserver: MutableLiveData<Post> = MutableLiveData()
    private val firestore: FirebaseFirestore = Firebase.firestore


    fun getById(id: String) {
        firestore.collection(POSTS_COLLECTION).document(id).get().addOnSuccessListener {
            val post: Post? = it.toObject(Post::class.java)
            post?.let { post ->
                post.id = id
                postObserver.value = post
            }
        }
    }

    fun add(post: Post, photoUri: Uri?): LiveData<String> {
        val postIdObserver: MutableLiveData<String> = MutableLiveData()
        photoUri?.let {
            val storageReference = Firebase.storage.reference.child(POSTS_COLLECTION)
            val time = SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(Date())
            val name = "${time}_${post.title}.jpg"
            storageReference.child(name).putFile(photoUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    post.urlImage = uri.toString()
                    firestore.collection(POSTS_COLLECTION).add(post)
                        .addOnSuccessListener { result ->
                            postIdObserver.value = result.id
                        }.addOnFailureListener {
                            postIdObserver.value = ""
                        }
                }
            }
        } ?: run {
            firestore.collection(POSTS_COLLECTION).add(post)
                .addOnSuccessListener { result ->
                    postIdObserver.value = result.id
                }.addOnFailureListener {
                    postIdObserver.value = ""
                }
        }
        return postIdObserver
    }

    fun update(post: Post, photoUri: Uri?): LiveData<Boolean> {
        val stateUpdateObserver: MutableLiveData<Boolean> = MutableLiveData()
        photoUri?.let {
            val storageReference = Firebase.storage.reference.child(POSTS_COLLECTION)
            val time = SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(Date())
            val name = "${time}_${post.title}.jpg"
            storageReference.child(name).putFile(photoUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    post.urlImage = uri.toString()
                    updateFireStore(post, stateUpdateObserver)
                }
            }
        } ?: run {
            updateFireStore(post, stateUpdateObserver)
        }
        return stateUpdateObserver
    }

    private fun updateFireStore(post: Post, stateUpdateObserver: MutableLiveData<Boolean>): Unit {
        firestore.collection(POSTS_COLLECTION).document(post.id).set(post)
            .addOnSuccessListener {
                stateUpdateObserver.value = true
            }.addOnFailureListener {
                stateUpdateObserver.value = false
            }
    }

    fun deleteFirestore(post: Post): LiveData<Boolean> {
        val stateDeleteObserver: MutableLiveData<Boolean> = MutableLiveData()
        firestore.collection(POSTS_COLLECTION).document(post.id).delete()
            .addOnSuccessListener {
                stateDeleteObserver.value = true
            }
            .addOnFailureListener {
                stateDeleteObserver.value = false
            }
//        TODO: cargar post
        return stateDeleteObserver
    }
}
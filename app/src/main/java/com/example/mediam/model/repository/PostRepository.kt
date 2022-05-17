package com.example.mediam.model.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediam.model.entity.Filter
import com.example.mediam.model.entity.Post
import com.example.mediam.model.entity.Topic
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class PostRepository(myContext: Context) {
    private val POSTS_COLLECTION: String = "posts"
    private val TOPIC_COLLECTION: String = "topics"
    var postsObserver: MutableLiveData<List<Post>> = MutableLiveData()
    var postObserver: MutableLiveData<Post> = MutableLiveData()
    private val firestore: FirebaseFirestore = Firebase.firestore


    fun getById(id: String) {
        lateinit var myPost:Post
        firestore.collection(POSTS_COLLECTION).document(id).get().addOnSuccessListener {
            it.let{
                myPost = it.toObject(Post::class.java)!!
                myPost.let { mp ->
                    mp.id = it.id
                    firestore.collection(TOPIC_COLLECTION).document(mp.idTopic).get()
                        .addOnSuccessListener {
                            it.let {
                                val myTopic:Topic = it.toObject(Topic::class.java)!!
                                mp.topic = myTopic.name
                            }
                            postObserver.value = mp
                        }
                }

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

    fun deleteFirestore(post: Post, idUser:String): LiveData<Boolean> {
        val stateDeleteObserver: MutableLiveData<Boolean> = MutableLiveData()
        firestore.collection(POSTS_COLLECTION).document(post.id).delete()
            .addOnSuccessListener {
                stateDeleteObserver.value = true
                loadPostsFirestore(idUser)
            }
            .addOnFailureListener {
                stateDeleteObserver.value = false
            }
        return stateDeleteObserver
    }

    fun loadPostsFirestore(id: String? = null) {
        val postList: ArrayList<Post> = arrayListOf()
        lateinit var myPost:Post
        if (id == null) {//si viene nulo, o sea, sin id, quiere decir que este metodo listará todos los posts
            //se usará para el Home. si se le envia id, entonces traerá solo los post del usuario en sesión.
            firestore.collection(POSTS_COLLECTION).get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        for (document in it.documents) {
                            myPost = document.toObject(Post::class.java)!!
                            myPost.let { mp ->
                                mp.id = document.id
                                firestore.collection(TOPIC_COLLECTION).document(mp.idTopic).get()
                                    .addOnSuccessListener {
                                        it.let {
                                            val myTopic:Topic = it.toObject(Topic::class.java)!!
                                            mp.topic = myTopic.name
                                            postList.add(mp)
                                        }
                                        postsObserver.value = postList
                                    }
                            }
                        }
                    }
                }
        } else {
            firestore.collection(POSTS_COLLECTION).whereEqualTo("idUser", id).get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        for (document in it.documents) {
                            myPost = document.toObject(Post::class.java)!!
                            myPost.let { mp ->
                                mp.id = document.id
                                firestore.collection(TOPIC_COLLECTION).document(mp.idTopic).get()
                                    .addOnSuccessListener {
                                        it.let {
                                            val myTopic:Topic = it.toObject(Topic::class.java)!!
                                            mp.topic = myTopic.name
                                            postList.add(mp)
                                        }
                                        postsObserver.value = postList
                                    }
                            }
                        }
                    }
                }
        }

    }


    fun loadPostByFilters(filter: Filter) {
        if (filter.title.isNotEmpty() && filter.idTopic.isNotEmpty()) {
            getByAllFilterValues(filter)
        } else if (filter.idTopic.isNotEmpty()) {
            getByIdTopic(filter)
        } else if (filter.title.isNotEmpty()) {
            getByTitle(filter)
        } else {
            loadPostsFirestore()
        }
    }

    private fun getByTitle(filter: Filter) {
        val postRef = firestore.collection(POSTS_COLLECTION)

        postRef
            .whereGreaterThanOrEqualTo("title", filter.title)
            .whereLessThan("title", filter.title + " z")

            .get().addOnSuccessListener {

                val postList: ArrayList<Post> = arrayListOf<Post>()
                if (!it.isEmpty) {
                    for (document in it.documents) {
                        val myPost: Post? = document.toObject(Post::class.java)
                        myPost?.let {
                            it.id = document.id
                            postList.add(it)
                        }
                    }
                }
                postsObserver.value = postList
            }.addOnFailureListener {
                postsObserver.value = arrayListOf<Post>()
                println(it.message)
            }
    }


    private fun getByIdTopic(filter: Filter) {
        val postRef = firestore.collection(POSTS_COLLECTION)
        postRef
            .whereEqualTo("idTopic", filter.idTopic)

            .get().addOnSuccessListener {

                val postList: ArrayList<Post> = arrayListOf<Post>()
                if (!it.isEmpty) {
                    for (document in it.documents) {
                        val myPost: Post? = document.toObject(Post::class.java)
                        myPost?.let {
                            it.id = document.id
                            postList.add(it)
                        }
                    }
                }
                postsObserver.value = postList
            }.addOnFailureListener {
                postsObserver.value = arrayListOf<Post>()
                println(it.message)
            }
    }


    private fun getByAllFilterValues(filter: Filter) {
        val postRef = firestore.collection(POSTS_COLLECTION)

        postRef
            .whereEqualTo("idTopic", filter.idTopic)
            .whereGreaterThanOrEqualTo("title", filter.title)
            .whereLessThan("title", filter.title + " z")

            .get().addOnSuccessListener {

                val postList: ArrayList<Post> = arrayListOf<Post>()
                if (!it.isEmpty) {
                    for (document in it.documents) {
                        val myPost: Post? = document.toObject(Post::class.java)
                        myPost?.let {
                            it.id = document.id
                            postList.add(it)
                        }
                    }
                }
                postsObserver.value = postList
            }.addOnFailureListener {
                postsObserver.value = arrayListOf<Post>()
                println(it.message)
            }
    }


}
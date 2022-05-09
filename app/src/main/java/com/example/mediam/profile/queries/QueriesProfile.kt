package com.example.mediam.profile.queries

import com.example.mediam.databinding.FragmentPerfilBinding
import com.example.mediam.model.entity.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class QueriesProfile {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val COLLECTION: String = "users"
    //val result:ArrayList<Int> = arrayListOf()

    //PENDIENTE PARA HACER CON CORRUTINAS
    fun retrieveInfoFollowUser(id:String, binding: FragmentPerfilBinding){
        firestore.collection(COLLECTION).document(id).get().addOnSuccessListener {
            val InfoUser: User? = it.toObject(User::class.java)
            InfoUser?.let {
                binding.followingCount.text = InfoUser.following.size.toString()
                binding.followersCount.text = InfoUser.followers.size.toString()
            }
        }
    }


    /*@DelicateCoroutinesApi
    fun getCountFollow(id:String):ArrayList<Int> = runBlocking {
        val job = GlobalScope.launch {
            retrieveInfoFollowUser(id)
        }
        job.join()
        return@runBlocking result
    }*/
}

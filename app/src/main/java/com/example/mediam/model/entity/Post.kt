package com.example.mediam.model.entity

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

class Post : Serializable {
    @JvmField
    @Exclude
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var idTopic: String = ""
    var idUser: String = ""

    @JvmField
    @PropertyName("url_image")
    var urlImage: String =
        "https://media-cdn.tripadvisor.com/media/photo-s/10/c4/23/16/highland-view-bed-and.jpg"


    @Exclude
    fun isComplete(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty() && idTopic.isNotEmpty() && idUser.isNotEmpty()
    }

    override fun toString(): String {
        return "Post(id='$id', title='$title', description='$description', idTopic='$idTopic', idUser='$idUser', urlImage='$urlImage')"
    }
}
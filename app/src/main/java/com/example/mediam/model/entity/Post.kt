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
    var idTopic: Int = 0
    var tag: String = ""

    @JvmField
    @PropertyName("url_image")
    var urlImage: String = ""
}
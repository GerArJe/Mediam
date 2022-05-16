package com.example.mediam.model.entity

import com.google.firebase.firestore.Exclude
import java.io.Serializable

class Topic : Serializable {
    @JvmField
    @Exclude
    var id: String = ""
    var name: String = ""


    override fun toString(): String {
        return name
    }
}
package com.example.mediam.model.entity

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

class User (
    @JvmField @Exclude
    var id: String = "",
    var documento: String = "",
    var nombre: String = "",
    var email: String = "",
    @JvmField @Exclude
    var password: String = "",
    @JvmField @PropertyName("url_photo")
    var photoUrl: String = "https://www.pngmart.com/files/10/User-Account-PNG-Clipart.png"
){

}
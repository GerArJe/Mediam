package com.example.mediam.model.entity

import java.io.Serializable

class Filter: Serializable {
    var title: String = ""
    var idTopic: String = ""

    override fun toString(): String {
        return "Filter(title='$title', idTopic='$idTopic')"
    }


}
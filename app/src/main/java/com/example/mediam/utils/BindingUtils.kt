package com.example.mediam.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load(url).into(imageView)
}

@InverseMethod("stringToInt")
fun intToString(value: Int): String {
    return value.toString()
}

@InverseMethod("intToString")
fun stringToInt(value: String): Int {
    if (value == "") {
        return 0
    }
    return Integer.parseInt(value)
}
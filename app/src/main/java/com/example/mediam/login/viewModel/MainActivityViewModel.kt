package com.example.mediam.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mediam.model.entity.User
import com.example.mediam.model.repository.UserRepository

class MainActivityViewModel : ViewModel(){
    var user: User = User(id="",documento = "",nombre = "",email = "",password = "")
    private val userRepository: UserRepository = UserRepository()

    fun login(): LiveData<User?> {
        return userRepository.login(user.email, user.password)
    }
}
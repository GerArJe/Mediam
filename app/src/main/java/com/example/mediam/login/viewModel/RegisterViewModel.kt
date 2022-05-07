package com.example.mediam.login.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mediam.model.entity.User
import com.example.mediam.model.repository.UserRepository


class RegisterViewModel: ViewModel() {
    var user: User = User("", "", "", "","")
    private val userRepository: UserRepository = UserRepository()

    fun singUp(): LiveData<User?> {
        return  userRepository.signUp(user, user.password)
    }
}
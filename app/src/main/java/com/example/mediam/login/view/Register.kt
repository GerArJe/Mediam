package com.example.mediam.login.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.post.view.Home
import com.example.mediam.R
import com.example.mediam.login.viewModel.RegisterViewModel
import com.example.mediam.databinding.ActivityRegisterBinding
import com.example.mediam.model.entity.User

class Register : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]


        binding.viewmodel = viewModel


        binding.btnRegister.setOnClickListener {
            viewModel.singUp().observe(this) {
                it?.let {
                    login(it)
                }?:run {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun login(user: User) {
        val preferences: SharedPreferences =
            getSharedPreferences("shad.pref", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean("login", true)
        editor.apply()

        val intentLogin = Intent(applicationContext, Home::class.java)
        intentLogin.apply {
            //putExtra("message", "Hola")
            putExtra("data", user.email)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intentLogin)
        Toast.makeText(this, "Inciando Sesion....", Toast.LENGTH_SHORT).show()
    }
}
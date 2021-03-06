package com.example.mediam.login.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mediam.home.view.MainNavigationActivity
import com.example.mediam.R
import com.example.mediam.databinding.ActivityMainBinding
import com.example.mediam.login.viewModel.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("shad.pref", MODE_PRIVATE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]


        binding.viewModel = viewModel


        if (preferences.getBoolean("login", false) && preferences.getString("idUser", "") != ""){
            goToHome()
        }else{
            println("Me quedo en home")
        }


        binding.btnRegister.setOnClickListener {
            val intentSignup = Intent(applicationContext, Register::class.java)
            startActivity(intentSignup)
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login().observe(this) {
                it?.let {
                    login(it.id, it.documento, it.nombre, it.photoUrl)
                    println("---------------")
                    println(it.id)
                } ?: run {
                    Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun login(id: String, doc:String, name:String, url:String) {
        val preferences: SharedPreferences = getSharedPreferences("shad.pref", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean("login", true)
        editor.putString("email", viewModel.user.email)
        editor.putString("name", name)
        editor.putString("idUser", id)
        editor.putString("doc", doc)
        editor.putString("url", url)
        editor.apply()
        goToHome()
    }

    private fun goToHome() {
        val intentLogin = Intent(applicationContext, MainNavigationActivity::class.java)
        startActivity(intentLogin)
        finish()
    }
}
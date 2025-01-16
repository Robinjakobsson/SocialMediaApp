package com.example.socialmediaapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.Repositories.FireBaseAuth
import com.example.socialmediaapp.Repositories.FireBaseDatabase

class AuthViewModel : ViewModel() {

    private val auth = FireBaseAuth()



    fun createAccount(email: String, password : String, username : String, profileImageUrl : String) {
        auth.createAccount(email,password,username,profileImageUrl)
    }

    fun signIn(email: String,password: String) {
        auth.signIn(email,password)
    }

}
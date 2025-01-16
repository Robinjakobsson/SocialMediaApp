package com.example.socialmediaapp.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.Repositories.FireBaseAuth
import com.example.socialmediaapp.Repositories.FireBaseDatabase

class AuthViewModel : ViewModel() {

    private val auth = FireBaseAuth()



    fun createAccount(email: String, password : String, username : String, imageUri : Uri) {
        auth.createAccount(email,password,username,imageUri)
    }

    fun signIn(email: String,password: String) {
        auth.signIn(email,password)
    }

}
package com.example.socialmediaapp.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.Model.User
import com.example.socialmediaapp.Repositories.FireBaseAuth
import com.example.socialmediaapp.Repositories.FireBaseDatabase
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

     private val auth = FireBaseAuth()



    fun createAccount(email: String, password : String, username : String, imageUri : Uri,onsuccess: () -> Unit,onFailure: (Exception) -> Unit) {
        auth.createAccount(email,password,username,imageUri,onsuccess,onFailure)
    }

    fun signIn(email: String,password: String,onsuccess : () -> Unit, onFailure: (Exception) -> Unit) {
        auth.signIn(email,password,onsuccess,onFailure)
    }

    fun getCurrentUser() : FirebaseUser? {
        return auth.auth.currentUser
    }

    fun getUserData(uid: String, onSuccess: (User) -> Unit, onFailure : (Exception) -> Unit) {
        auth.getUserData(uid,onSuccess,onFailure)
    }

}
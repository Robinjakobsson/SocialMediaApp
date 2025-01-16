package com.example.socialmediaapp.Repositories

import android.util.Log
import com.example.socialmediaapp.Model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore

class FireBaseAuth {

    val auth = Firebase.auth
    private val db = FireBaseDatabase()

    fun createAccount(email: String, password: String, username: String, profileImageUrl: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { isCreated ->
                if (isCreated.isSuccessful) {

                    val currentUser = auth.currentUser
                    val uid = currentUser?.uid

                    if (uid != null) {
                        db.addUser(username, uid, profileImageUrl)
                    }

                } else {
                    Log.d("FireBaseAuth", "User $username Not Created.")
                }
            }
    }


    fun signIn(email : String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        Log.d("FireBaseAuth","User ${auth.currentUser} signed in Successfully!")
                    }
                }else {
                    Log.d("FireBaseAuth","Login Failed")
                }
            }

    }
    fun getCurrentUser() = auth.currentUser
}
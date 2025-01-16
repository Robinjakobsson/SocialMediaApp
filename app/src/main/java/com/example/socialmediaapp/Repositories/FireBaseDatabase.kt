package com.example.socialmediaapp.Repositories

import android.util.Log
import com.example.socialmediaapp.Model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FireBaseDatabase {

        private val db = Firebase.firestore


     fun addUser(userName : String, uid : String, profileImageUrl : String) {
       val user = User(userName, uid, profileImageUrl)

        db.collection("users")
            .document(uid)
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Database","User ${user.username} has Been added to the database!")
                }else {
                    Log.d("Database", "User ${user.username} Has not been added")
                }
            }

    }
}
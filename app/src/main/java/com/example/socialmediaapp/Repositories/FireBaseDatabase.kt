package com.example.socialmediaapp.Repositories

import android.util.Log
import com.example.socialmediaapp.Model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FireBaseDatabase {

        private val db = Firebase.firestore


     suspend fun addUser(userName : String, uid : String, profileImageUrl : String) {
         val user = User(userName, uid, profileImageUrl)

         try {
             db.collection("users")
                 .document(uid)
                 .set(user)
                 .await()

             Log.d("FireBaseDatabase","User ${user.username} has been added to the database!")
         }catch (e : Exception) {
             Log.d("FireBaseDataBase", "User was not added to database!")
             throw e
         }



    }
}
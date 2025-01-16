package com.example.socialmediaapp.Repositories

import android.net.Uri
import android.util.Log
import com.example.socialmediaapp.Model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class FireBaseAuth {
    private val storage = Firebase.storage
    val auth = Firebase.auth
    private val db = FireBaseDatabase()

    fun createAccount(email: String, password: String, username: String, imageUri: Uri) {
        // Skapa en referens för profilbilden i Firebase Storage
        val profileImageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")

        // Ladda upp bilden till Firebase Storage
        profileImageRef.putFile(imageUri)
            .addOnSuccessListener {
                // När uppladdningen är klar, hämta downloadUrl
                profileImageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    // Skapa användarkonto med e-post och lösenord
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Om användaren skapas, hämta den aktuella användaren
                                val currentUser = auth.currentUser
                                val uid = currentUser?.uid

                                if (uid != null) {
                                    // Lägg till användaren i databasen
                                    db.addUser(username, uid, downloadUrl.toString())
                                }
                            } else {
                                Log.d("FireBaseAuth", "User $username Not Created.")
                            }
                        }
                }.addOnFailureListener { e ->
                    // Hantera eventuella fel när downloadUrl inte kan hämtas
                    Log.d("FireBaseStorage", "Failed to download profile image URL: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                // Hantera eventuella fel vid uppladdning av bilden
                Log.d("FireBaseStorage", "Failed to upload image: ${e.message}")
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
package com.example.socialmediaapp.Repositories

import android.net.Uri
import android.util.Log
import com.example.socialmediaapp.Model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class FireBaseAuth {
    private val storage = Firebase.storage
    val auth = Firebase.auth
    private val db = FireBaseDatabase()

     fun createAccount(email: String, password: String, username: String, imageUri: Uri, onsuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
        try {
            uploadProfileImage(imageUri)

            val uid = createFireBaseUser(email,password)

            saveUserToDatabase(username,uid,imageUri.toString())

            withContext(Dispatchers.Main) {
                onsuccess()
            }

        }catch (e : Exception) {
            withContext(Dispatchers.Main) {
                onFailure(e)
                }
            }
        }
    }



    fun signIn(email : String, password: String, onsuccess: () -> Unit, onFailure : (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = signInFireBaseUser(email,password)

                withContext(Dispatchers.Main) {
                    Log.d("FireBaseAuth", "User logged in Successfully!")
                    onsuccess()
                }
            }catch (e : Exception) {
                val errorMessage = getSignInErrorMessage(e)
                withContext(Dispatchers.Main) {
                    Log.d("FireBaseAuth","Failed to login User $errorMessage")
                    onFailure(Exception(errorMessage))
                }
            }
        }

    }
    fun getCurrentUser() = auth.currentUser

    suspend fun uploadProfileImage(imageUri: Uri): String {
        val profileImageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")
        val uploadTask = profileImageRef.putFile(imageUri).await()
        return profileImageRef.downloadUrl.toString()
    }

    suspend fun createFireBaseUser(email: String,password: String): String {
        val authResult = auth.createUserWithEmailAndPassword(email,password).await()
        return authResult.user?.uid ?: throw Exception("User Creation failed: UID is null")
    }
    suspend fun saveUserToDatabase(username: String,uid: String,imageUrl : String) {
        db.addUser(username,uid,imageUrl)
    }
    suspend fun signInFireBaseUser(email: String,password: String): FirebaseUser {
        val authResult = auth.signInWithEmailAndPassword(email,password).await()
        return authResult.user ?: throw Exception("Failed to retrieve User..")
    }
    fun getSignInErrorMessage(exception: Exception?): String {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
            is FirebaseAuthUserCollisionException -> "This email is already registered"
            else -> exception?.localizedMessage ?: "Sign in failed. Please try again"
        }
    }
}
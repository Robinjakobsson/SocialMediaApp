package com.example.socialmediaapp.Repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.socialmediaapp.Model.Post
import com.example.socialmediaapp.Model.User
import com.example.socialmediaapp.Model.UserWithPosts
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FireBaseStorage {

    private val storage = Firebase.storage
    private val auth = FireBaseAuth()
    private val db = Firebase.firestore
    private val currentUser = auth.getCurrentUser()
    private val _userWithPosts = MutableLiveData<List<UserWithPosts>?>()
    val userWithPosts: MutableLiveData<List<UserWithPosts>?> get() = _userWithPosts


        suspend fun uploadPost(imageUri: Uri, caption: String) {
            try {
                val imageFileName = "posts/${UUID.randomUUID()}.jpg"
                val storageRef = storage.reference.child(imageFileName)

                val uploadTask = storageRef.putFile(imageUri).await()

                val downloadUrl = storageRef.downloadUrl.await()

                savePostToDatabase(downloadUrl.toString(), caption)
            } catch (e: Exception) {
                throw e
            }
        }

    suspend fun savePostToDatabase(imageUrl: String, caption: String) {
        val postId = UUID.randomUUID().toString()
        val post = hashMapOf(
            "postId" to postId,
            "userId" to (currentUser?.uid ?: ""),
            "userName" to (currentUser?.displayName ?: "Anonymous"),
            "userImageUrl" to (currentUser?.photoUrl.toString()),
            "imageUrl" to imageUrl,
            "postCaption" to caption,
            "timestamp" to System.currentTimeMillis()
        )

        try {
            db.collection("posts").document(postId).set(post).await()
            Log.d("FirebaseStorage", "Successfully created post!")
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Failed to save post to database", e)
        }
    }

    fun setupRealTimeListeners() {
        val userscollection = db.collection("users")
        val postCollection = db.collection("posts")

        userscollection.addSnapshotListener { userSnapshot, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }
            val users = userSnapshot?.toObjects(User::class.java)

            postCollection.addSnapshotListener { postSnapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }
                val posts = postSnapshot?.toObjects(Post::class.java)

                val postsGroupedById = posts?.groupBy { it.userId }

                val userWithPostList = users?.map { user ->
                    UserWithPosts(
                        user, posts = postsGroupedById?.get(user.userid) ?: emptyList()
                    )
                }
                _userWithPosts.postValue(userWithPostList)
            }
        }
    }
    fun personalPostsListener(uid : String, onComplete: (MutableList<Post>) -> Unit) {
        val postRef = db.collection("posts").whereEqualTo("userId", uid)
        postRef.addSnapshotListener { snapShot, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }
            val postsList = mutableListOf<Post>()

            for (doc in snapShot?.documents!!) {
                val post = doc.toObject(Post::class.java)

                if (post != null) {
                    postsList.add(post)
                }
            }
            Log.d("FireBaseStorage","Fetched posts : ${postsList.size}")
            onComplete(postsList)
        }
    }
}
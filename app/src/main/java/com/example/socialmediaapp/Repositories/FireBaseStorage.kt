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

    val storage = Firebase.storage
    private val auth = FireBaseAuth()
    private val db = Firebase.firestore
    private val currentUser = auth.getCurrentUser()

    private val _posts = MutableLiveData<MutableList<Post>>()


    private val _userWithPosts = MutableLiveData<List<UserWithPosts>?>()
    val userWithPosts: MutableLiveData<List<UserWithPosts>?> get() = _userWithPosts

    init {
        postsListener()
    }

        suspend fun uploadPost(imageUri: Uri, caption: String) {
            try {
                val imageFileName = "posts/${UUID.randomUUID()}.jpg"
                val storageRef = storage.reference.child(imageFileName)

                // Ladda upp bilden
                val uploadTask = storageRef.putFile(imageUri).await()

                // Hämta nedladdnings-URL
                val downloadUrl = storageRef.downloadUrl.await()

                // Spara posten i databasen
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

    private fun postsListener() {
        db.collection("posts").addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    val postsList = mutableListOf<Post>()
                    for (doc in snapshot.documents) {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) {
                            post.postId = doc.id
                            postsList.add(post)
                        }
                    }
                    _posts.postValue(postsList)
                }
                else {
                    Log.d("FireBaseStorage","Problem fetching Posts from Database $error")
                }
            }
    }
    fun setupRealTimeListeners() {
        // Hämtar båda Kollektionerna
        val userscollection = db.collection("users")
        val postCollection = db.collection("posts")

        //Lägger till Snapshot Listener på User kollektionen
        userscollection.addSnapshotListener { userSnapshot, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }
            // Gör om alla users till objekt
            val users = userSnapshot?.toObjects(User::class.java)


            postCollection.addSnapshotListener { postSnapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }
                val posts = postSnapshot?.toObjects(Post::class.java)

                // Grupperar Posts efter UserId
                val postsGroupedById = posts?.groupBy { it.userId }

                // Kopplar Ihop User med Post
                val userWithPostList = users?.map { user ->
                    UserWithPosts(user,posts = postsGroupedById?.get(user.userid) ?: emptyList()
                    )
                }
                _userWithPosts.postValue(userWithPostList)
                }
            }
        }
}
package com.example.socialmediaapp.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.Model.Post
import com.example.socialmediaapp.Model.UserWithPosts
import com.example.socialmediaapp.Repositories.FireBaseStorage
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch


class PostViewModel : ViewModel() {
    private val storage = FireBaseStorage()

    val personalPosts = MutableLiveData<MutableList<Post>>()

    private val _postUploadSuccess = MutableLiveData<Boolean>()
    val postUploadSuccess : LiveData<Boolean> get() = _postUploadSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage


    val userWithPosts : LiveData<List<UserWithPosts>?>get() = storage.userWithPosts

    init {
        storage.setupRealTimeListeners()
    }

     fun uploadPost(imageUri: Uri, caption : String) {
        viewModelScope.launch {
        storage.uploadPost(imageUri,caption)
        }
    }
    fun getPersonalPosts(uid : String) {
        storage.personalPostsListener(uid) { posts ->
            personalPosts.postValue(posts)
        }
    }
}
package com.example.socialmediaapp.Model

data class Post(
    var postId: String = "",
    val userId: String = "",
    val userName: String? = null,
    val userImageUrl: String? = null,
    val imageUrl: String = "",
    val postCaption: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {

}
package com.example.socialmediaapp.Model

data class User(
    val username : String = "",
    val userid : String = "",
    val profileImageUrl : String = "",
    var followers : String = "",
    var follows : String = ""
) {
}
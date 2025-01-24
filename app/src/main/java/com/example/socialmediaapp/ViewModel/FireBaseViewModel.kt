package com.example.socialmediaapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.Model.Post
import com.example.socialmediaapp.Repositories.FireBaseAuth
import com.example.socialmediaapp.Repositories.FireBaseDatabase

class FireBaseViewModel : ViewModel() {

    private val db = FireBaseDatabase()
    private val auth = FireBaseAuth()


}
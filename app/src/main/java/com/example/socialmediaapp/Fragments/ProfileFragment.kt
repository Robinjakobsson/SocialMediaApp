package com.example.socialmediaapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Grid
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.socialmediaapp.Adapters.ProfilePostAdapter
import com.example.socialmediaapp.Model.Post
import com.example.socialmediaapp.Model.User
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ViewModel.AuthViewModel
import com.example.socialmediaapp.ViewModel.FireBaseViewModel
import com.example.socialmediaapp.ViewModel.PostViewModel
import com.example.socialmediaapp.databinding.FragmentProfileBinding
import java.io.Serializable




class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private lateinit var auth: AuthViewModel
    private lateinit var postVm: PostViewModel
    private lateinit var adapter: ProfilePostAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = ViewModelProvider(this)[AuthViewModel::class.java]
        postVm = ViewModelProvider(this)[PostViewModel::class.java]

        val fireBaseUser = auth.getCurrentUser()
        recyclerViewSetup()

        fireBaseUser?.uid?.let { uid ->
            Log.d("UID", "User ID: $uid")
            postVm.getPersonalPosts(uid)
        }
        postVm.personalPosts.observe(viewLifecycleOwner) { posts ->
            if (posts.isEmpty()) {
                Log.d("Profile", "Posts is empty")
            }
            adapter.updateData(posts)
        }


        fireBaseUser.let { user ->
            if (user != null) {
                auth.getUserData(user.uid, onSuccess = { userdata ->
                    updateUI(userdata)

                }, onFailure = { exception ->
                    exception.printStackTrace()
                })
            }
        }
    }

    private fun updateUI(user: User) {
        binding?.apply {
            profileName.text = user.username
            followersCount.text = user.followers
            followingCount.text = user.follows
            Glide.with(this@ProfileFragment)
                .load(user.profileImageUrl)
                .into(profileImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun recyclerViewSetup() {
        adapter = ProfilePostAdapter(mutableListOf())
        binding?.postsRecyclerView?.adapter = adapter
        binding?.postsRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)

    }
}

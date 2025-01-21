package com.example.socialmediaapp.View

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.socialmediaapp.Fragments.HomeFragment
import com.example.socialmediaapp.Fragments.PostFragment
import com.example.socialmediaapp.Fragments.ProfileFragment
import com.example.socialmediaapp.Model.User
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ViewModel.AuthViewModel
import com.example.socialmediaapp.databinding.ActivityPostsBinding

class PostsActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val postFragment = PostFragment()
    private val profileFragment = ProfileFragment()
    private lateinit var binding: ActivityPostsBinding
    private lateinit var auth : AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = ViewModelProvider(this)[AuthViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setCurrentFragment(homeFragment)
        bottomNavigationBarLogic()

    }
    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,fragment)
                addToBackStack(null)
                .commit()
        }
    }
    private fun bottomNavigationBarLogic(){
        binding.bottomNavBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment)
                    true
                }
                R.id.profile -> {
                    setCurrentFragment(profileFragment)
                    true
                }
                R.id.new_post -> {
                    setCurrentFragment(postFragment)
                    true
                }

                else -> false
            }
        }
    }
}
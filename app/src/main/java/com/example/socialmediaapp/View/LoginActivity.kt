package com.example.socialmediaapp.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ViewModel.AuthViewModel
import com.example.socialmediaapp.ViewModel.FireBaseViewModel
import com.example.socialmediaapp.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = ViewModelProvider(this).get(AuthViewModel::class.java)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.loginButton.setOnClickListener { login() }

        binding.registerTv.setOnClickListener { navigateToSignUpActivity() }
    }

    private fun login() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        binding.loginButton.visibility = View.VISIBLE

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty()) {
            Toast.makeText(this,"Please enter E-mail", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this,"Enter a password", Toast.LENGTH_SHORT).show()
            return
        }else {

            auth.signIn(email, password)
            binding.loginButton.visibility = View.GONE
            navigateToPostsActivity()
        }
    }

    private fun navigateToSignUpActivity() {
        val newIntent = Intent(this, SignUpActivity::class.java)
        startActivity(newIntent)
    }

    private fun navigateToPostsActivity(){
        val newIntent = Intent(this,PostsActivity::class.java)
        startActivity(newIntent)
    }
}
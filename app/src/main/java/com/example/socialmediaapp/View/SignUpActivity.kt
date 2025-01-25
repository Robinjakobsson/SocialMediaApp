package com.example.socialmediaapp.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ViewModel.AuthViewModel
import com.example.socialmediaapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth: AuthViewModel
    private var selectedImageUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.registerButton.setOnClickListener { register() }

        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) {uri : Uri? ->
                if (uri != null) {
                    selectedImageUri = uri
                    binding.profileImageChooser.setImageURI(selectedImageUri)
                }
            }

        binding.profileImageChooser.setOnClickListener { pickImageLauncher.launch("image/*") }


    }

    private fun register(){
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val bio = binding.biography.text.toString()

        binding.loginProgressBar.visibility = View.VISIBLE

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()){
            Toast.makeText(this,"All fields are required!",Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty()) {
            Toast.makeText(this,"Please enter E-mail",Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this,"Enter a password",Toast.LENGTH_SHORT).show()
            return
        }
        if (username.isEmpty()){
            Toast.makeText(this,"Enter a username!",Toast.LENGTH_SHORT).show()
            return
        }else {

            selectedImageUri?.let { auth.createAccount(email, password, username, it,bio, onsuccess = {
                Toast.makeText(this, "Account with $username Created!", Toast.LENGTH_SHORT).show()
                binding.loginProgressBar.visibility = View.GONE
                navigateToLoginActivity()
            }, onFailure = {
                binding.loginProgressBar.visibility = View.GONE
                Toast.makeText(this,"Account Was not created",Toast.LENGTH_SHORT).show()
            }) }

        }
    }
    fun navigateToLoginActivity(){
        val newIntent = Intent(this,LoginActivity::class.java)
        startActivity(newIntent)
    }
}
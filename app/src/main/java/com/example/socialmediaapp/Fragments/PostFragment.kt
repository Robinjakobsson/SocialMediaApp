package com.example.socialmediaapp.Fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.socialmediaapp.Adapters.PostAdapter
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ViewModel.FireBaseViewModel
import com.example.socialmediaapp.ViewModel.PostViewModel
import com.example.socialmediaapp.databinding.FragmentPostBinding


class PostFragment : Fragment() {
    private lateinit var postVm  : PostViewModel
    private var binding : FragmentPostBinding? = null
    private var selectedImageUri : Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postVm = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    selectedImageUri = uri
                    binding?.imagePreview?.setImageURI(uri)
                }
            }

        binding?.selectImageButton?.setOnClickListener { pickImageLauncher.launch("image/*") }

        binding?.uploadButton?.setOnClickListener {uploadPhoto()}


        postVm.postUploadSuccess.observe(viewLifecycleOwner, {success ->
            if (success) {
                Toast.makeText(requireContext(), "Post Uploaded Successfully!", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(requireContext(), "Failed to upload Post!", Toast.LENGTH_SHORT).show()
            }
        })
        postVm.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun uploadPhoto(){
        val caption = binding?.captionEditText?.text.toString()

        if (selectedImageUri != null && caption.isNotBlank()) {
            postVm.uploadPost(selectedImageUri!!, caption)
        } else {
            Toast.makeText(
                requireContext(),
                "Please select an image and provide a caption", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
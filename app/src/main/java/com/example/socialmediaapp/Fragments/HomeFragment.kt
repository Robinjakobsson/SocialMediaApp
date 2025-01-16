package com.example.socialmediaapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediaapp.Adapters.PostAdapter
import com.example.socialmediaapp.Model.Post
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ViewModel.PostViewModel
import com.example.socialmediaapp.databinding.FragmentHomeBinding
import com.example.socialmediaapp.databinding.FragmentPostBinding

class HomeFragment : Fragment() {
    private lateinit var adapter : PostAdapter
    private var binding : FragmentHomeBinding? = null
    private lateinit var postVm : PostViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewSetup()

        postVm = ViewModelProvider(this)[PostViewModel::class.java]

        postVm.userWithPosts.observe(viewLifecycleOwner) { userWithPostsList ->
            if (userWithPostsList != null) {
                adapter.updateData(userWithPostsList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
    private fun recyclerViewSetup(){
        adapter = PostAdapter(mutableListOf())
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())

    }

}
package com.example.socialmediaapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.Model.Post
import com.example.socialmediaapp.Model.UserWithPosts
import com.example.socialmediaapp.R

class ProfilePostAdapter(val posts : MutableList<Post>
) : RecyclerView.Adapter<ProfilePostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val postImage : ImageView = itemView.findViewById(R.id.post_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_post,parent,false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
       return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        Log.d("bind","$position ${post.imageUrl}")

        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .placeholder(R.drawable.logo)
            .centerCrop()
            .into(holder.postImage)
        }
    fun updateData(newList : List<Post>) {
        Log.d("Adapter","${newList.size}")
        posts.clear()
        posts.addAll(newList)
        notifyDataSetChanged()
    }
    }







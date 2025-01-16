package com.example.socialmediaapp.Adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaapp.Model.Post
import com.example.socialmediaapp.Model.User
import com.example.socialmediaapp.Model.UserWithPosts
import com.example.socialmediaapp.R

class PostAdapter(
    val posts : MutableList<UserWithPosts>,

) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val caption : TextView = itemView.findViewById(R.id.caption_text)
        val profileImage : ImageView = itemView.findViewById(R.id.profile_image)
        val userName : TextView = itemView.findViewById(R.id.user_name)
        val postImage : ImageView = itemView.findViewById(R.id.post_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item,parent,false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val userWithPost = posts[position]

       val post = userWithPost.posts.firstOrNull()
        post?.let {
            holder.caption.text = it.postCaption
            holder.userName.text = userWithPost.user.username

            Glide.with(holder.itemView.context)
                .load(it.imageUrl)
                .into(holder.postImage)
        }






    }
    fun updateData(newList : List<UserWithPosts>) {
        posts.clear()
        posts.addAll(newList)
        notifyDataSetChanged()
    }
}
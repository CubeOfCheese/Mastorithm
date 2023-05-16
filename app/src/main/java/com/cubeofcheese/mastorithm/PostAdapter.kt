package com.cubeofcheese.mastorithm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cubeofcheese.mastorithm.models.PostModel
import com.squareup.picasso.Picasso

class PostAdapter(private val postList : ArrayList<PostModel>) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_post, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.boostedByText.text = currentItem.boostedByMessage
        holder.displayName.text = currentItem.displayName
        holder.username.text = currentItem.username
        holder.postContent.text = currentItem.postContent
        Picasso
            .get()
            .load(currentItem.imagePreview)
            .into(holder.imagePreview);
        Picasso
            .get()
            .load(currentItem.avatar)
            .placeholder(R.drawable.default_profile_picture)
            .into(holder.avatar);
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val boostedByText : TextView = itemView.findViewById(R.id.boostedByText)
        val displayName : TextView = itemView.findViewById(R.id.displayName)
        val username : TextView = itemView.findViewById(R.id.username)
        val avatar : ImageView = itemView.findViewById(R.id.avatar)
        val postContent : TextView = itemView.findViewById(R.id.postContent)
        val imagePreview : ImageView = itemView.findViewById(R.id.imagePreview)
    }

}
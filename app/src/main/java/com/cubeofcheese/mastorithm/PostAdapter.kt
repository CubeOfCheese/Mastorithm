package com.cubeofcheese.mastorithm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
        holder.displayName.text = currentItem.displayName
        holder.username.text = currentItem.username
        holder.postContent.text = currentItem.postContent
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val displayName : TextView = itemView.findViewById(R.id.displayName)
        val username : TextView = itemView.findViewById(R.id.username)
        val postContent : TextView = itemView.findViewById(R.id.postContent)
    }

}
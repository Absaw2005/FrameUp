package com.example.instagramclone.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.databinding.MyPostDesignBinding
import com.example.instagramclone.models.Post
import com.squareup.picasso.Picasso

class PostRvAdapter(var context:android.content.Context, private var postList:ArrayList<Post>) :RecyclerView.Adapter<PostRvAdapter.ViewHolder>(){
    inner class ViewHolder(var binding:MyPostDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=MyPostDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(postList[position].postUrl).into(holder.binding.postImg)
    }

}
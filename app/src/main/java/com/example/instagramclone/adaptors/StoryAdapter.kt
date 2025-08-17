package com.example.instagramclone.adaptors


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.databinding.StoryzdesignBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.User

class StoryAdapter(var context:android.content.Context, private var storyList:ArrayList<User>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>(){
    inner class ViewHolder(var binding: StoryzdesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= StoryzdesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(storyList[position].image).into(holder.binding.ProfileImg)
       holder.binding.followingName.text=storyList[position].uid.toString()




    }

}
package com.example.instagramclone.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramclone.databinding.MyPostDesignBinding
import com.example.instagramclone.models.Reel

class MyReelsAdapter(var context:android.content.Context, private var reelList:ArrayList<Reel>) :
    RecyclerView.Adapter<MyReelsAdapter.ViewHolder>(){


    inner class ViewHolder(var binding: MyPostDesignBinding): RecyclerView.ViewHolder(binding.root)



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Glide.with(context).load(reelList[position].videoUrl)
           .diskCacheStrategy(DiskCacheStrategy.ALL)
           .into(holder.binding.postImg)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= MyPostDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }



}
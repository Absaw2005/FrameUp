package com.example.instagramclone.adaptors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.activity.ProfileActivity
import com.example.instagramclone.databinding.HomepostRvDesignBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.Utils
import com.example.instagramclone.utils.AppViewModel
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow


class  HomePostAdapter(var context:android.content.Context, private var postList:ArrayList<Post>) :
    RecyclerView.Adapter<HomePostAdapter.ViewHolder>(){
    inner class ViewHolder(var binding: HomepostRvDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= HomepostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int ) {

        Firebase.database.reference.child("User").child(postList[position].uploaderUid.toString()).addValueEventListener(
            object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue<User>()


                    //getting likes
                    Firebase.database.reference.child("Post")
                        .child(postList[position].uploaderUid.toString())
                        .child(postList[position].time.toString()).child("like")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val likesCount = snapshot.value as Long?
                                holder.binding.likeTxt.text = likesCount.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })

                    val text = postList[position].time?.let { TimeAgo.using(it.toLong()) }
                    Glide.with(context).load(user?.image).placeholder(R.drawable.user)
                        .into(holder.binding.ProfileImg)
                    holder.binding.name.text = user?.name
                    Glide.with(context).load(postList[position].postUrl)
                        .placeholder(R.drawable.icon).into(holder.binding.postImg)
                    holder.binding.time.text = text
                    holder.binding.uid.text = postList[position].uploaderUid
                    holder.binding.Postcaption.text = postList[position].caption
                    holder.binding.postId.text = postList[position].postId.toString()


                    // On like btn clicked


                    Firebase.database.reference.child("Post")
                        .child(postList[position].uploaderUid.toString())
                        .child(postList[position].time.toString()).child("likes")
                        .child(Utils.getUserUid())
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // User already liked post, show as liked
                                    holder.binding.like.setImageResource(R.drawable.liked)
                                    holder.binding.like.setOnClickListener {
                                        holder.binding.like.setImageResource(R.drawable.heart)
                                        Firebase.database.reference.child("Post")
                                            .child(postList[position].uploaderUid.toString())
                                            .child(postList[position].time.toString())
                                            .child("likes").child(Utils.getUserUid()).removeValue()
                                        Firebase.database.reference.child("Post")
                                            .child(postList[position].uploaderUid.toString())
                                            .child(postList[position].time.toString()).child("like")
                                            .runTransaction(object : Transaction.Handler {
                                                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                                    val currentValue = mutableData.value as Long?
                                                    val newValue = (currentValue ?: 0) - 1
                                                    mutableData.value = newValue
                                                    return Transaction.success(mutableData)
                                                }

                                                override fun onComplete(
                                                    databaseError: DatabaseError?,
                                                    p1: Boolean,
                                                    p2: DataSnapshot?
                                                ) {
                                                    // Handle completion
                                                }
                                            })

                                    }
                                } else {

                                    holder.binding.like.setOnClickListener {
                                        holder.binding.like.setImageResource(R.drawable.liked)
                                        Firebase.database.reference.child("Post")
                                            .child(postList[position].uploaderUid.toString())
                                            .child(postList[position].time.toString())
                                            .child("likes").child(Utils.getUserUid()).setValue(true)
                                        Firebase.database.reference.child("Post")
                                            .child(postList[position].uploaderUid.toString())
                                            .child(postList[position].time.toString()).child("like")
                                            .runTransaction(object : Transaction.Handler {
                                                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                                    val currentValue = mutableData.value as Long?
                                                    val newValue = (currentValue ?: 0) + 1
                                                    mutableData.value = newValue
                                                    return Transaction.success(mutableData)
                                                }

                                                override fun onComplete(
                                                    databaseError: DatabaseError?,
                                                    p1: Boolean,
                                                    p2: DataSnapshot?
                                                ) {
                                                    // Handle completion
                                                }
                                            })

                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })


                    // On follow button clicked
                    if (postList[position].uploaderUid.toString() == Utils.getUserUid()) {
                        holder.binding.FollowBtn.visibility = View.GONE
                    } else {
                        Firebase.database.reference.child("User").child(Utils.getUserUid())
                            .child("following").child(postList[position].uploaderUid.toString())
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        // User already liked post, show as liked
                                        holder.binding.FollowBtn.visibility= View.GONE



                                    } else {
                                        holder.binding.FollowBtn.visibility= View.VISIBLE
                                        holder.binding.FollowBtn.setOnClickListener {
                                            Firebase.database.reference.child("User")
                                                .child(Utils.getUserUid()).child("following")
                                                .child(postList[position].uploaderUid.toString())
                                                .setValue(true)
                                            Firebase.database.reference.child("User")
                                                .child(postList[position].uploaderUid.toString())
                                                .child("followers").child(Utils.getUserUid())
                                                .setValue(true)
                                            Toast.makeText(context," Started following ${postList[position].uploaderUid.toString()}",
                                                Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle error
                                }
                            })


                        // going to profile
                        holder.binding.ProfileImg.setOnClickListener {
                            val userId = holder.binding.uid.text.toString()
                            val intent = Intent(context, ProfileActivity::class.java)
                            intent.putExtra("userId", userId)
                            context.startActivity(intent)

                        }


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}
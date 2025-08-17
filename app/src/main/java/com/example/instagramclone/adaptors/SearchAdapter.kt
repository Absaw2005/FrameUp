package com.example.instagramclone.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.activity.ProfileActivity
import com.example.instagramclone.databinding.SearchRvBinding
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.Utils
import com.google.firebase.database.*

class SearchAdapter(
    private val context: Context,
    private var userList: ArrayList<User>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]

        Glide.with(context)
            .load(user.image)
            .placeholder(R.drawable.user_icon)
            .into(holder.binding.ProfileImg)

        holder.binding.name.text = user.name
        holder.binding.profileEmail.text = user.email

        val currentUserUid = Utils.getUserUid()
        val targetUserUid = user.uid ?: return

        if (currentUserUid == targetUserUid) {
            holder.binding.FollowBtn.visibility = View.GONE
        } else {
            val followRef = FirebaseDatabase.getInstance().reference
                .child("User")
                .child(currentUserUid)
                .child("following")
                .child(targetUserUid)

            followRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        holder.binding.FollowBtn.visibility = View.GONE
                    } else {
                        holder.binding.FollowBtn.visibility = View.VISIBLE
                        holder.binding.FollowBtn.setOnClickListener {
                            val database = FirebaseDatabase.getInstance().reference

                            database.child("User").child(currentUserUid).child("following")
                                .child(targetUserUid).setValue(true)
                            database.child("User").child(targetUserUid).child("followers")
                                .child(currentUserUid).setValue(true)

                            Toast.makeText(context, "Started following ${user.name}", Toast.LENGTH_SHORT).show()
                            holder.binding.FollowBtn.visibility = View.GONE
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Open profile on click
        holder.binding.ProfileImg.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("userId", targetUserUid)
            context.startActivity(intent)
        }
    }
}

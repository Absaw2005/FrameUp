package com.example.instagramclone.utils

import androidx.lifecycle.ViewModel
import com.example.instagramclone.R
import com.example.instagramclone.databinding.HomepostRvDesignBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow


class AppViewModel:ViewModel() {

    private val _isLiked = MutableStateFlow(false)
    val liked = _isLiked


    fun showingLikedOrNot(uploaderUid:String,time:String,binding:HomepostRvDesignBinding){
        Firebase.database.reference.child("Post").child(uploaderUid).child(time).child("likes").child(Utils.getUserUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User already liked post, show as liked

                   binding.like.setImageResource(R.drawable.liked)
                    binding.like.setOnClickListener {
                        binding.like.setImageResource(R.drawable.heart)
                        Firebase.database.reference.child("Post").child(uploaderUid).child(time).child("like").runTransaction(object : Transaction.Handler {
                            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                val currentValue = mutableData.value as Long?
                                val newValue = (currentValue ?: 0) - 1
                                mutableData.value = newValue
                                return Transaction.success(mutableData)
                            }

                            override fun onComplete(databaseError: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                                // Handle completion
                            }
                        })

                    }
                } else {

                    binding.like.setOnClickListener {
                        binding.like.setImageResource(R.drawable.liked)
                        Firebase.database.reference.child("Post").child(uploaderUid).child(time).child("like").runTransaction(object : Transaction.Handler {
                            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                val currentValue = mutableData.value as Long?
                                val newValue = (currentValue ?: 0) + 1
                                mutableData.value = newValue
                                return Transaction.success(mutableData)
                            }

                            override fun onComplete(databaseError: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
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
    }




}
package com.example.instagramclone.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramclone.R
import com.example.instagramclone.adaptors.ViewPagerAdapter
import com.example.instagramclone.adaptors.ViewPagerAdapter2
import com.example.instagramclone.databinding.ActivityProfileBinding
import com.example.instagramclone.databinding.HomepostRvDesignBinding
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

 userId = intent.getStringExtra("userId").toString()
        getUserData()
        setupFollowButton()

        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialToolbar3.setNavigationOnClickListener {
            finish()
        }

    }

    private fun setupFollowButton() {
        val currentUid = Utils.getUserUid()
        Firebase.database.reference.child("User").child(currentUid)
            .child("following").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isFollowing = snapshot.exists()
                    binding.editBtn.text = if (isFollowing) "Following" else "Follow"

                    binding.editBtn.setOnClickListener {
                        if (isFollowing) {
                            showUnfollowDialog(currentUid, userId)
                        } else {
                            followUser(currentUid, userId)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun followUser(currentUid: String, targetUid: String) {
        Firebase.database.reference.child("User").child(currentUid)
            .child("following").child(targetUid).setValue(true)

        Firebase.database.reference.child("User").child(targetUid)
            .child("followers").child(currentUid).setValue(true)

        Toast.makeText(this, "Started following", Toast.LENGTH_SHORT).show()
    }

    private fun unfollowUser(currentUid: String, targetUid: String) {
        Firebase.database.reference.child("User").child(currentUid)
            .child("following").child(targetUid).removeValue()

        Firebase.database.reference.child("User").child(targetUid)
            .child("followers").child(currentUid).removeValue()

        Toast.makeText(this, "Unfollowed user", Toast.LENGTH_SHORT).show()
    }

    private fun showUnfollowDialog(currentUid: String, targetUid: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Unfollow User")
        builder.setMessage("Are you sure you want to unfollow this user?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            unfollowUser(currentUid, targetUid)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }




   private fun getUserData() {
        FirebaseDatabase.getInstance().getReference("User").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
           override fun onDataChange(dataSnapshot: DataSnapshot) {
              val user = dataSnapshot.getValue(User::class.java)
                val userName=user?.name
                val userEmail=user?.email

               binding.profileEmail.text=userEmail
               binding.name.text=userName
               binding.materialToolbar3.title=userName

                if (!user!!.image.isNullOrEmpty()){
                    Picasso.get().load(user.image).into(binding.ProfileImg)
                }

           }
           override fun onCancelled(databaseError: DatabaseError) {
             // Handle error
           }
        })
    }


    override fun onStart() {
        super.onStart()

        val tabLayout: TabLayout =binding.tableLayout
        val vp2: ViewPager2 =binding.vp2

        binding.vp2.adapter= ViewPagerAdapter2(this,userId)
        TabLayoutMediator(tabLayout,vp2){tab,position->
            when(position){
                0->tab.text="Posts"
                1->tab.text="Reels"
            }

        }.attach()







    }


}
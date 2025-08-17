package com.example.instagramclone.Fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.R
import com.example.instagramclone.activity.PostActivity
import com.example.instagramclone.adaptors.HomePostAdapter
import com.example.instagramclone.adaptors.StoryAdapter
import com.example.instagramclone.databinding.FragmentHomeBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var post:Post
    private lateinit var user:User



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        post=Post()
        user=User()
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)
        setHasOptionsMenu(true)

        //adapter
        val postList=ArrayList<Post>()
        val adapter= HomePostAdapter(requireContext(),postList)
        binding.HomePostRv.layoutManager= LinearLayoutManager(requireContext())
        binding.HomePostRv.adapter=adapter


      //  gettingPosts
        FirebaseDatabase.getInstance().getReference().child("ScrollPost").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                val temperList=ArrayList<Post>()
                for (dataSnapshot in snapshot.children) {
                    val post: Post? = dataSnapshot.getValue(Post::class.java)
                    if (post != null) {
                        temperList.add(post)
                    }
                }
                postList.addAll(temperList)
                postList.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })




        //profileImage
        Firebase.database.reference.child("User").child(Firebase.auth.currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user=snapshot.getValue<User>()

                    if (!user!!.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.ProfileImg)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }


    // Increase like
    private fun increaseLike(postId: String) {
        val likesRef = FirebaseDatabase.getInstance().getReference("Post").child(Utils.getUserUid()).child("like")
        likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentLikes = snapshot.value as Long
                val newLikes = currentLikes + 1
                likesRef.setValue(newLikes)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Decrease like
    private fun decreaseLike(postId: String) {
        val likesRef = FirebaseDatabase.getInstance().getReference("Post").child(Utils.getUserUid()).child("like")
        likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentLikes = snapshot.value as Long
                val newLikes = currentLikes - 1
                likesRef.setValue(newLikes)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
        super.onCreateOptionsMenu(menu, inflater)
    }

}
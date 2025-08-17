package com.example.instagramclone.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.R
import com.example.instagramclone.adaptors.PostRvAdapter
import com.example.instagramclone.adaptors.SearchAdapter
import com.example.instagramclone.databinding.FragmentSearchBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.USER_NODE
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class searchFragment : Fragment() {

    private lateinit var binding:FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentSearchBinding.inflate(inflater, container, false)

        val userList=ArrayList<User>()
        val adapter= SearchAdapter(requireContext(),userList)
        binding.searchRv.layoutManager=LinearLayoutManager(requireContext())
        binding.searchRv.adapter=adapter

        FirebaseDatabase.getInstance().getReference().child(USER_NODE).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temperList = ArrayList<User>()
                for (dataSnapshot in snapshot.children) {


                        val user: User? = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            temperList.add(user)
                        }
                    }
                    userList.addAll(temperList)
                    adapter.notifyDataSetChanged()
                }


            override fun onCancelled(error: DatabaseError) {
            }
        })

       val text=binding.searchView.text.toString()






        return binding.root
    }


}
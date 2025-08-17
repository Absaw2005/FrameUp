package com.example.instagramclone.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.R
import com.example.instagramclone.adaptors.MyReelsAdapter
import com.example.instagramclone.adaptors.ReelAdapter
import com.example.instagramclone.databinding.FragmentReelBinding
import com.example.instagramclone.models.Reel
import com.example.instagramclone.utils.REEL
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ReelFragment : Fragment() {

    private lateinit var binding: FragmentReelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= FragmentReelBinding.inflate(inflater, container, false)

        val reelList=ArrayList<Reel>()
        val adapter= ReelAdapter(requireContext(),reelList)
        binding.viewpager.adapter=adapter

        FirebaseDatabase.getInstance().getReference().child("ScrollReels").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList=ArrayList<Reel>()
                reelList.clear()
                for (dataSnapshot in snapshot.children) {
                    val reel: Reel? = dataSnapshot.getValue(Reel::class.java)
                    if (reel != null) {
                        tempList.add(reel)
                    }
                }
                reelList.addAll(tempList)
                reelList.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })









        return binding.root
    }

    companion object {

    }
}




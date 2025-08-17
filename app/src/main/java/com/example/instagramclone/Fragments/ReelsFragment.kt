package com.example.instagramclone.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.R
import com.example.instagramclone.adaptors.MyReelsAdapter
import com.example.instagramclone.adaptors.PostRvAdapter
import com.example.instagramclone.databinding.FragmentPostBinding
import com.example.instagramclone.databinding.FragmentReelBinding
import com.example.instagramclone.databinding.FragmentReelsBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.Reel
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.REEL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ReelsFragment : Fragment() {


    private lateinit var binding: FragmentReelsBinding
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString("userId") ?: FirebaseAuth.getInstance().uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        binding= FragmentReelsBinding.inflate(inflater, container, false)

        val reelList=ArrayList<Reel>()
        val adapter= MyReelsAdapter(requireContext(),reelList)
        binding.rvReels.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvReels.adapter=adapter

        FirebaseDatabase.getInstance().getReference().child(REEL).child(userId).addValueEventListener(object :
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

        fun newInstance(userId: String): ReelsFragment{
            val fragment = ReelsFragment()
            val bundle = Bundle()
            bundle.putString("userId", userId)
            fragment.arguments = bundle
            return fragment
        }

    }
}


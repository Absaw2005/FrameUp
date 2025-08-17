package com.example.instagramclone.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramclone.adaptors.PostRvAdapter
import com.example.instagramclone.databinding.FragmentPostBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.utils.POST
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString("userId") ?: FirebaseAuth.getInstance().uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)

        val postList = ArrayList<Post>()
        val adapter = PostRvAdapter(requireContext(), postList)
        binding.rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter

        FirebaseDatabase.getInstance().getReference(POST).child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    postList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val post: Post? = dataSnapshot.getValue(Post::class.java)
                        post?.let { postList.add(it) }
                    }
                    postList.reverse()
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return binding.root
    }

    companion object {
        fun newInstance(userId: String): PostFragment {
            val fragment = PostFragment()
            val bundle = Bundle()
            bundle.putString("userId", userId)
            fragment.arguments = bundle
            return fragment
        }
    }
}

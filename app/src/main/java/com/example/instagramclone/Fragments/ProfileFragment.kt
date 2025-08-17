package com.example.instagramclone.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramclone.R
import com.example.instagramclone.activity.LoginActivity
import com.example.instagramclone.adaptors.ViewPagerAdapter
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.example.instagramclone.models.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= FragmentProfileBinding.inflate(inflater, container, false)

          binding.editBtn.setOnClickListener {
              findNavController().navigate(R.id.action_Profile_to_editFragment)
          }

        binding.logoutBtn.setOnClickListener {
            showLogOutDialog()
        }


        return binding.root


    }

    private fun showLogOutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("LogOut")
        builder.setMessage("Are you sure you want to LogOUT")
        builder.setPositiveButton("Yes") { dialog, _ ->
           FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }




    override fun onStart() {
        super.onStart()

        val tabLayout: TabLayout =binding.tableLayout
        val vp2:ViewPager2=binding.vp2

        binding.vp2.adapter=ViewPagerAdapter(this)
        TabLayoutMediator(tabLayout,vp2){tab,position->
            when(position){
                0->tab.text="My Post"
                1->tab.text="My Reels"
            }

        }.attach()



        Firebase.database.reference.child("User").child(Firebase.auth.currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user=snapshot.getValue<User>()
                    binding.name.text=user?.name.toString()
                    binding.profileEmail.text=user?.email.toString()

                    if (!user!!.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.ProfileImg)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })




    }

}
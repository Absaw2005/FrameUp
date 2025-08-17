package com.example.instagramclone.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.instagramclone.R
import com.example.instagramclone.databinding.FragmentEditBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.POST
import com.example.instagramclone.utils.USER_NODE
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.example.instagramclone.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso


class EditFragment : Fragment() {

private lateinit var binding: FragmentEditBinding
private lateinit var user:User
private var changedImg: String? = null

    private var launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri.let {
            if (uri != null) {
                Utils.uploadImage(uri, USER_PROFILE_FOLDER)
                {
                    changedImg=it
                    binding.profileImg.setImageURI(uri)

                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        user=User()
        binding=FragmentEditBinding.inflate(layoutInflater)
        retrieveUserData()
        binding.profileImg.setOnClickListener {
            launcher.launch("image/*")
        }

        return binding.root



    }



    private fun retrieveUserData() {
        FirebaseDatabase.getInstance().getReference().child(USER_NODE).child(Firebase.auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {

                    val user=snapshot.getValue<User>()

               val    name=user?.name.toString()
                 val   email=user?.email.toString()
                  val  password=user?.password.toString()

                  binding.userName.setText(name)
                    binding.password.setText(password)
                    binding.email.setText(email)

                    if (!user!!.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.profileImg)
                    }

                    binding.saveData.setOnClickListener {
                       user.name=binding.userName.text.toString()
                       user.email=binding.email.text.toString()
                        user.password=binding.password.text.toString()
                        if (changedImg == null) {
                            user.image=user.image
                        }
                        else{
                        user.image=changedImg

}
                        FirebaseDatabase.getInstance().getReference().child(USER_NODE).child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user).addOnSuccessListener {
                            Toast.makeText(requireContext(),"Changes saved",Toast.LENGTH_SHORT).show()
                        }

                        findNavController().navigate(R.id.action_editFragment_to_Profile)
                    }

                }

            }


            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


}
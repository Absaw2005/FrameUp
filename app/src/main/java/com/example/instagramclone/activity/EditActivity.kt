package com.example.instagramclone.activity

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.databinding.ActivityEditBinding
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.example.instagramclone.utils.Utils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding


    private lateinit var user: User
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri.let {
            if (uri != null) {
              Utils. uploadImage(uri, USER_PROFILE_FOLDER)
                {
                    user.image = it
                    binding.profileImg.setImageURI(uri)
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User()


        binding.signup.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString()
            val name = binding.userName.text.toString()


                            user.email = email
                            user.password = password
                            user.name = name
                            FirebaseDatabase.getInstance().reference.child("User")
                                .child(Firebase.auth.currentUser!!.uid).setValue(user)
                                .addOnSuccessListener {


                                }

                            finish()
                        }






        binding.plusIcon.setOnClickListener {
            launcher.launch("image/*")
        }



    }
}


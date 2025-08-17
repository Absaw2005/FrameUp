package com.example.instagramclone.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.databinding.ActivitySignUpBinding
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.USER_PROFILE_FOLDER
import com.example.instagramclone.utils.Utils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding


    private lateinit var user:User
    private var launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri.let {
            if (uri != null) {
               Utils. uploadImage(uri, USER_PROFILE_FOLDER)
                {
                    user.image=it
                    binding.profileImg.setImageURI(uri)
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user= User()


        binding.signup.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString()
            val name = binding.userName.text.toString()

            if (email.isBlank() || password.isBlank() || name.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            user.email=email
                            user.password=password
                            user.name=name
                            user.uid=Utils.getUserUid()


                            FirebaseDatabase.getInstance().reference.child("User").child(Firebase.auth.currentUser!!.uid).setValue(user)
                                .addOnSuccessListener {

                                    Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT)
                                        .show()

                                }
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }


            }
        }

        binding.plusIcon.setOnClickListener {
            launcher.launch("image/*")
        }




        }

}
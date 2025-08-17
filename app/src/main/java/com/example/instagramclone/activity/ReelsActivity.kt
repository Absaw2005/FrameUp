package com.example.instagramclone.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramclone.databinding.ActivityReelsBinding
import com.example.instagramclone.models.Reel
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.REEL
import com.example.instagramclone.utils.REEl_FOLDER
import com.example.instagramclone.utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class ReelsActivity : AppCompatActivity() {


    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityReelsBinding
    private lateinit var reel: Reel
    override fun onCreate(savedInstanceState: Bundle?) {

        var videoUri: String = null.toString()
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {

               Utils. uploadReel(uri, REEl_FOLDER, progressDialog) {
                    if (it != null) {
                        reel.videoUrl = it
                        Glide.with(this).load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.PostReel)
                    }
                }


            }
        }



        super.onCreate(savedInstanceState)
        binding = ActivityReelsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reel = Reel()

        progressDialog = ProgressDialog(this)


        binding.PostReel.setOnClickListener {
            launcher.launch("video/*")
        }


        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.postBtn.setOnClickListener {
            if (binding.caption.text.toString().isBlank()) {
                Toast.makeText(this, "Enter a caption", Toast.LENGTH_SHORT).show()
            } else {

                Firebase.database.reference.child("User").child(Firebase.auth.currentUser!!.uid)
                    .addValueEventListener(
                        object : ValueEventListener {


                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user = snapshot.getValue<User>()

                                reel.caption = binding.caption.text.toString()
                                reel.profileLink=user?.image
                                reel.profileName=user?.name
                                reel.time=System.currentTimeMillis().toString()
                                reel.uid=FirebaseAuth.getInstance().currentUser!!.uid
                                reel.like=0


                                FirebaseDatabase.getInstance().reference.child(REEL)
                                    .child(Firebase.auth.currentUser!!.uid).child(reel.time!!).setValue(reel)
                                    .addOnCompleteListener { it ->
                                        if (it.isSuccessful) {
                                            FirebaseDatabase.getInstance().reference.child("ScrollReels")
                                                .child(reel.time!!).setValue(reel)
                                                .addOnCompleteListener {


                                                    Toast.makeText(
                                                        this@ReelsActivity,
                                                        "Reel Added Successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    finish()

                                                }
                                        }

                                    }


                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
            }




            binding.cancelBtn.setOnClickListener {
                finish()
            }

        }


    }
}

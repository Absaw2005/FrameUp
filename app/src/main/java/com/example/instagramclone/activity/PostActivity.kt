package com.example.instagramclone.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.databinding.ActivityPostBinding
import com.example.instagramclone.models.Post
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.POST_FOLDER
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

class PostActivity : AppCompatActivity() {
private lateinit var post: Post
    private lateinit var binding:ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        val imageUri:String= null.toString()




       val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
                uri->
            uri?.let {

                Utils.uploadImage(uri, POST_FOLDER){
                    if (it!=null){
                        binding.PostImg.setImageURI(uri)
                       post.postUrl=it
                    }
                }



            }
        }
        super.onCreate(savedInstanceState)
        binding=ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        post=Post()





        binding.PostImg.setOnClickListener {
            launcher.launch("image/*")
        }
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        val text=intent?.getStringExtra("text")
        if (text!=null) {
            binding.materialToolbar.setTitle("Add Story")
            binding.postLine.text = "Share a story"
            binding.postBtn.setOnClickListener {
                if (binding.caption.text.toString().isBlank()) {
                    Toast.makeText(this, "Enter a caption", Toast.LENGTH_SHORT).show()
                } else {
                    Firebase.database.reference.child("User").child(Firebase.auth.currentUser!!.uid)
                        .addValueEventListener(
                            object : ValueEventListener {


                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue<User>()

                                    post.name = user?.name
                                    post.caption = binding.caption.text.toString()
                                    post.time = System.currentTimeMillis().toString()
                                    post.like = 0
                                    post.profilePic = user?.image
                                    post.postId = Utils.generateRandomString()
                                    post.uploaderUid = FirebaseAuth.getInstance().currentUser!!.uid


                                    FirebaseDatabase.getInstance().reference.child("Story")
                                        .push()
                                        .setValue(post).addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Toast.makeText(
                                                    this@PostActivity,
                                                    "Story Added Successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                intent.putExtra("Story","Story Uploaded")
                                                finish()


                                            } else {
                                                Toast.makeText(
                                                    this@PostActivity,
                                                    it.exception?.localizedMessage,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                }


            }
        }
        else {

            binding.postBtn.setOnClickListener {
                if (binding.caption.text.toString().isBlank()) {
                    Toast.makeText(this, "Enter a caption", Toast.LENGTH_SHORT).show()
                } else {

                    Firebase.database.reference.child("User").child(Firebase.auth.currentUser!!.uid)
                        .addValueEventListener(
                            object : ValueEventListener {


                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue<User>()

                                    post.name = user?.name
                                    post.caption = binding.caption.text.toString()
                                    post.time = System.currentTimeMillis().toString()
                                    post.like = 0
                                    post.profilePic = user?.image
                                    post.postId = Utils.generateRandomString()
                                    post.uploaderUid = FirebaseAuth.getInstance().currentUser!!.uid


                                    FirebaseDatabase.getInstance().reference.child("Post")
                                        .child(Firebase.auth.currentUser!!.uid).child(post.time!!)
                                        .setValue(post).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            FirebaseDatabase.getInstance().reference.child("ScrollPost")
                                                .child(post.time!!).setValue(post)

                                            Toast.makeText(
                                                this@PostActivity,
                                                "Post Added Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()

                                        } else {
                                            Toast.makeText(
                                                this@PostActivity,
                                                it.exception?.localizedMessage,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })


                }

            }
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

    }
}
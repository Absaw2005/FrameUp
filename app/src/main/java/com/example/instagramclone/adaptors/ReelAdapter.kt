package com.example.instagramclone.adaptors

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.R
import com.example.instagramclone.activity.ProfileActivity
import com.example.instagramclone.databinding.ReelBdBinding
import com.example.instagramclone.models.Reel
import com.example.instagramclone.models.User
import com.example.instagramclone.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


private var isPause:Boolean=false
class ReelAdapter(var context:android.content.Context, private var reelList:ArrayList<Reel>) :
    RecyclerView.Adapter<ReelAdapter.ViewHolder>(){


    inner class ViewHolder(var binding: ReelBdBinding): RecyclerView.ViewHolder(binding.root)



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Firebase.database.reference.child("User").child(reelList[position].uid.toString()).addValueEventListener(
            object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    val user=snapshot.getValue<User>()

        Picasso.get().load(user?.image).placeholder(R.drawable.user).into(holder.binding.ProfileImg)
        holder.binding.caption.text = reelList[position].caption
        holder.binding.uploaderImg.text=user?.name.toString()
        holder.binding.id.text=reelList[position].uid
        holder.binding.reelLike.setOnClickListener {
            holder.binding.reelLike.setImageResource(R.drawable.liked)
        }
        holder.binding.videoView.setVideoPath(reelList[position].videoUrl)
        holder.binding.videoView.setOnPreparedListener{mp->
            mp.isLooping = true
            holder.binding.progressBar.isVisible=false
            holder.binding.videoView.requestFocus()
            holder.binding.videoView.start()



            holder.binding.videoView.setOnClickListener {
                if (isPause){
                    holder.binding.videoView.start()
                    holder.binding.playPause.setImageResource(R.drawable.play)
                    holder.binding.playPause.visibility= View.VISIBLE
                    isPause=false
                }else{
                    holder.binding.videoView.pause()
                    isPause=true
                    holder.binding.playPause.visibility= View.VISIBLE
                    holder.binding.playPause.setImageResource(R.drawable.pause)
                }

                holder.binding.playPause.postDelayed({
                    holder.binding.playPause.visibility = View.GONE
                }, 2000)

            }

            //getting likes
            Firebase.database.reference.child("Reel").child(reelList[position].uid.toString()).child(reelList[position].time.toString()).child("like")
                .addValueEventListener( object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val likesCount = snapshot.value as Long?
                        holder.binding.likeCount.text = likesCount.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })


            Firebase.database.reference.child("Reel").child(reelList[position].uid.toString())
                .child(reelList[position].time.toString()).child("likes").child(Utils.getUserUid())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // User already liked post, show as liked
                            holder.binding.reelLike.setImageResource(R.drawable.liked)
                            holder. binding.reelLike.setOnClickListener {
                                holder.  binding.reelLike.setImageResource(R.drawable.heart)
                                Firebase.database.reference.child("Reel").child(reelList[position].uid.toString())
                                    .child(reelList[position].time.toString()).child("likes").child(Utils.getUserUid()).removeValue()
                                Firebase.database.reference.child("Reel").child(reelList[position].uid.toString()).child(reelList[position].time.toString()).child("like").runTransaction(object : Transaction.Handler {
                                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                        val currentValue = mutableData.value as Long?
                                        val newValue = (currentValue ?: 0) - 1
                                        mutableData.value = newValue
                                        return Transaction.success(mutableData)
                                    }

                                    override fun onComplete(databaseError: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                                        // Handle completion
                                    }
                                })

                            }
                        } else {

                            holder.binding.reelLike.setOnClickListener {
                                holder.  binding.reelLike.setImageResource(R.drawable.liked)
                                Firebase.database.reference.child("Reel").child(reelList[position].uid.toString()).child(reelList[position].time.toString()).child("likes").child(Utils.getUserUid()).setValue(true)
                                Firebase.database.reference.child("Reel").child(reelList[position].uid.toString()).child(reelList[position].time.toString()).child("like").runTransaction(object : Transaction.Handler {
                                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                        val currentValue = mutableData.value as Long?
                                        val newValue = (currentValue ?: 0) + 1
                                        mutableData.value = newValue
                                        return Transaction.success(mutableData)
                                    }

                                    override fun onComplete(databaseError: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                                        // Handle completion
                                    }
                                })

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })



            holder.binding.ProfileImg.setOnClickListener {
                val userId=holder.binding.id.text.toString()
                val intent= Intent(context, ProfileActivity::class.java)
                intent.putExtra("userId",userId)
                context.startActivity(intent)


            }

            }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ReelBdBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }





}
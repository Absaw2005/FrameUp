package com.example.instagramclone.utils

import android.app.ProgressDialog
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

object Utils {
    fun uploadImage(uri: Uri, folderName: String, callback: (String) -> Unit) {
        var imageUrl: String? = null
        FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
            .putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    imageUrl = it.toString()
                    callback(imageUrl!!)
                }
            }


    }

    fun uploadReel(
        uri: Uri,
        folderName: String,
        progressDialog: ProgressDialog,
        callback: (String) -> Unit
    ) {
        var imageUrl: String? = null
        progressDialog.setTitle("Uploading....")
        progressDialog.show()
        FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
            .putFile(uri)
            .addOnSuccessListener { it ->
                it.storage.downloadUrl.addOnSuccessListener {
                    imageUrl = it.toString()
                    progressDialog.dismiss()
                    callback(imageUrl!!)
                }
            }
            .addOnProgressListener {
                val uploadedValue: Long = (it.bytesTransferred / it.totalByteCount) * 100
                progressDialog.setMessage("Uploaded $uploadedValue %")
            }


    }

    fun getUserUid(): String {
       return FirebaseAuth.getInstance().currentUser!!.uid
    }


    fun getCurrentDateTime(): String {
        return System.currentTimeMillis().toString()
    }

    fun generateRandomString(): String {
            val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return List(20) { charset.random() }.joinToString("")
    }





}
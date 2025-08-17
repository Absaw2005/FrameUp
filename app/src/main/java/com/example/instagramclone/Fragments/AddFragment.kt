package com.example.instagramclone.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone.activity.PostActivity
import com.example.instagramclone.activity.ReelsActivity
import com.example.instagramclone.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentAddBinding.inflate(inflater, container, false)

        binding.UploadImg.setOnClickListener{
            activity?.startActivity(Intent(requireContext(), PostActivity::class.java))

        }

        binding.uploadReels.setOnClickListener{
            activity?.startActivity(Intent(requireContext(), ReelsActivity::class.java))

        }


        return binding.root





    }


    }

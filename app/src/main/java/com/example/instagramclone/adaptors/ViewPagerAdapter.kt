package com.example.instagramclone.adaptors

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.instagramclone.Fragments.PostFragment
import com.example.instagramclone.Fragments.ProfileFragment
import com.example.instagramclone.Fragments.ReelsFragment

class ViewPagerAdapter(fm: ProfileFragment): FragmentStateAdapter(fm){
    override fun getItemCount(): Int {
      return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->PostFragment()
           1->ReelsFragment()
           else->PostFragment()
       }
    }

}

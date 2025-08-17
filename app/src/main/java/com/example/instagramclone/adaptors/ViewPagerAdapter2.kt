package com.example.instagramclone.adaptors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.instagramclone.Fragments.PostFragment
import com.example.instagramclone.Fragments.ReelsFragment

class ViewPagerAdapter2(
    fragmentActivity: FragmentActivity,
    private val userId: String
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostFragment.newInstance(userId)
            1 -> ReelsFragment.newInstance(userId)  // Make sure ReelFragment also supports newInstance(userId)
            else -> Fragment()
        }
    }
}

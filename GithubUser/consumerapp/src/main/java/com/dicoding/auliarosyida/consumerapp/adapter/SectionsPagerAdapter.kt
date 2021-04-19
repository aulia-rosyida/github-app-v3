package com.dicoding.auliarosyida.githubuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.auliarosyida.githubuser.entity.User
import com.dicoding.auliarosyida.githubuser.fragment.FollowersFragment
import com.dicoding.auliarosyida.githubuser.fragment.ProfileFragment

class SectionsPagerAdapter(activity: AppCompatActivity, detailUser: User) : FragmentStateAdapter(activity) {

    var tempUser: User = detailUser

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ProfileFragment(tempUser)
            1 -> fragment = FollowersFragment.newInstance(1, "${tempUser.username}")
            2 -> fragment = FollowersFragment.newInstance(2, "${tempUser.username}")
        }
        return fragment as Fragment
    }

}
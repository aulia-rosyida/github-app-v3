package com.dicoding.auliarosyida.githubuser.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.auliarosyida.githubuser.R
import com.dicoding.auliarosyida.githubuser.databinding.FragmentProfileBinding
import com.dicoding.auliarosyida.githubuser.entity.User
import com.loopj.android.http.AsyncHttpClient.log

class ProfileFragment (detailUser: User) : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = requireNotNull(_binding)
    var user: User = detailUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(user.photo)
            .apply(RequestOptions().override(550, 550))
            .into(binding.imgReceivedPhoto)
        binding.tvReceivedUsername.text = "@${user.username}"
        binding.tvReceivedName.text = user.name?: user.username
        binding.tvReceivedLocation.text = user.location?: "unknown location"
        binding.tvReceivedCompany.text = user.company?: "unknown company"
        binding.tvReceivedRepo.text = user.repository.toString()
        binding.tvReceivedFollowers.text = user.followers.toString()
        binding.tvReceivedFollowing.text = user.following.toString()
        binding.repo.text = getString(R.string.tag_repo)
        binding.follower.text = getString(R.string.tag_followers)
        binding.following.text = getString(R.string.tag_following)

        var statusFav = false
        setStatusFav(statusFav)
        binding.favBtn.setOnClickListener{
            statusFav = !statusFav
            setStatusFav(statusFav)
        }
    }

    override fun onDestroyView() {
        // Do not store the binding instance in a field, if not required.
        super.onDestroyView()
        _binding = null
    }

    private fun setStatusFav(status: Boolean) {
        if (status) {
            log.d("USER ADAPTER :", "STATUS ON")
            binding.favBtn.setColorFilter(Color.MAGENTA)
        } else {
            log.d("USER ADAPTER :", "STATUS OFF")
            binding.favBtn.setColorFilter(Color.GRAY)
        }
    }
}
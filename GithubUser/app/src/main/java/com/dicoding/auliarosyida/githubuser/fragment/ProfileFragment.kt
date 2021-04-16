package com.dicoding.auliarosyida.githubuser.fragment

import android.content.ContentValues
import android.content.Context
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
import com.dicoding.auliarosyida.githubuser.db.UserDbContract
import com.dicoding.auliarosyida.githubuser.db.UserGithubHelper
import com.dicoding.auliarosyida.githubuser.entity.User
import com.loopj.android.http.AsyncHttpClient.log

class ProfileFragment (detailUser: User) : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = requireNotNull(_binding)
    var user: User = detailUser
    private var position: Int = 0
//    private lateinit var userGithubHelper: UserGithubHelper

    // companion object {
    //     const val EXTRA_NOTE = "extra_note"
    //     const val EXTRA_POSITION = "extra_position"
    //     const val REQUEST_ADD = 100
    //     const val RESULT_ADD = 101
    //     const val REQUEST_UPDATE = 200
    //     const val RESULT_UPDATE = 201
    //     const val RESULT_DELETE = 301
    //     const val ALERT_DIALOG_CLOSE = 10
    //     const val ALERT_DIALOG_DELETE = 20
    // }

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
        binding.tvReceivedRepo.text = user.repository
        binding.tvReceivedFollowers.text = user.followers
        binding.tvReceivedFollowing.text = user.following
        binding.repo.text = getString(R.string.tag_repo)
        binding.follower.text = getString(R.string.tag_followers)
        binding.following.text = getString(R.string.tag_following)

        // val thisContext = requireContext()
//        userGithubHelper = UserGithubHelper.getInstance(thisContext)
//        userGithubHelper.open()
        position = user.id

        var statusFav = user.isFavorited
         setStatusFav(statusFav)
    
         binding.favBtn.setOnClickListener{
             statusFav = !statusFav
             setStatusFav(statusFav)
             user.isFavorited = statusFav
             log.d("ISFAV USER :", "$statusFav")
         }
    }

    override fun onDestroyView() {
        // Do not store the binding instance in a field, if not required.
//        userGithubHelper.close()
        super.onDestroyView()
        _binding = null
    }

    private fun setStatusFav(status: Boolean) {
        if (status) {
            log.d("USER ADAPTER :", "STATUS ON")
            binding.favBtn.setColorFilter(Color.MAGENTA)
//            insertFavoriteUser()
        } else {
            log.d("USER ADAPTER :", "STATUS OFF")
            binding.favBtn.setColorFilter(Color.GRAY)
//            userGithubHelper.deleteById(position.toString()).toLong()
        }
    }

//     private fun insertFavoriteUser() {
//         val values = ContentValues()
//         values.put(UserDbContract.UserDbColumns.COL_USERNAME, user.username)
//         values.put(UserDbContract.UserDbColumns.COL_NAME, user.name)
//         values.put(UserDbContract.UserDbColumns.COL_LOCATION, user.location)
//         values.put(UserDbContract.UserDbColumns.COL_REPOSITORY, user.repository)
//         values.put(UserDbContract.UserDbColumns.COL_COMPANY, user.company)
//         values.put(UserDbContract.UserDbColumns.COL_FOLLOWER, user.followers)
//         values.put(UserDbContract.UserDbColumns.COL_FOLLOWING, user.following)
//         values.put(UserDbContract.UserDbColumns.COL_PHOTO, user.photo)
//         log.d("USER INSERT :", "masuk ke insert")

// //        userGithubHelper.insert(values)
//     }
}
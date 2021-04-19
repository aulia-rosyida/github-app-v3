package com.dicoding.auliarosyida.githubuser.fragment

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.auliarosyida.githubuser.R
import com.dicoding.auliarosyida.githubuser.databinding.FragmentProfileBinding
import com.dicoding.auliarosyida.githubuser.db.UserDbContract
import com.dicoding.auliarosyida.githubuser.db.UserGithubHelper
import com.dicoding.auliarosyida.githubuser.entity.User
import com.dicoding.auliarosyida.githubuser.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfileFragment (detailUser: User) : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null

    private val binding: FragmentProfileBinding
        get() = requireNotNull(_binding)

    var user: User = detailUser
    var uname = detailUser.username
    private lateinit var userGithubHelper: UserGithubHelper

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

        var statusFav = user.isFavorited

        //untuk cek sudah ada dalam db atau belum
        GlobalScope.launch(Dispatchers.Main) {
            val thisContext = requireContext()
            userGithubHelper = UserGithubHelper.getInstance(thisContext)
            userGithubHelper.open()

            try {
                val deferredFavorite = async(Dispatchers.IO) {
                    val cursor = userGithubHelper.queryByUname(uname)
                    MappingHelper.mapCursorToArrayList(cursor)
                }

                val favorite = deferredFavorite.await()
                if (favorite.size > 0) {
                    user.isFavorited = true
                    statusFav = true
                    binding.favBtn.setColorFilter(Color.MAGENTA)
                }
            }finally {
                userGithubHelper.close()
            }
        }

        binding.favBtn.setOnClickListener {
            statusFav = !statusFav
            setStatusFav(statusFav)
            user.isFavorited = statusFav
        }
    }

    

    override fun onDestroyView() {
        // Do not store the binding instance in a field, if not required.
        super.onDestroyView()
        _binding = null
    }

    private fun setStatusFav(status: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            val thisContext = requireContext()
            userGithubHelper = UserGithubHelper.getInstance(thisContext)
            userGithubHelper.open()
            try {
                if (status) {
                    binding.favBtn.setColorFilter(Color.MAGENTA)

                    var valuesTemp = valueFavoriteUser()
                    val insertResult = userGithubHelper.insert(valuesTemp)
                    if (insertResult > 0) {
                        Toast.makeText(thisContext, "Add to Favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(thisContext, "Failed to Favorite", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.favBtn.setColorFilter(Color.GRAY)
                    val deleteResult = userGithubHelper.deleteByUname(uname).toLong()
                    if (deleteResult > 0) {
                        Toast.makeText(thisContext, "delete from Favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(thisContext, "Failed to Favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            }finally {
                userGithubHelper.close()
            }
        }
    }

     private fun valueFavoriteUser() : ContentValues{
         val values = ContentValues()
         values.put(UserDbContract.UserDbColumns.COL_USERNAME, user.username)
         values.put(UserDbContract.UserDbColumns.COL_NAME, user.name)
         values.put(UserDbContract.UserDbColumns.COL_LOCATION, user.location)
         values.put(UserDbContract.UserDbColumns.COL_REPOSITORY, user.repository)
         values.put(UserDbContract.UserDbColumns.COL_COMPANY, user.company)
         values.put(UserDbContract.UserDbColumns.COL_FOLLOWER, user.followers)
         values.put(UserDbContract.UserDbColumns.COL_FOLLOWING, user.following)
         values.put(UserDbContract.UserDbColumns.COL_PHOTO, user.photo)

         return values
     }
}
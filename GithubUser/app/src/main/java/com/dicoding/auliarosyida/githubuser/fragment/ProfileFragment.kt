package com.dicoding.auliarosyida.githubuser.fragment

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.auliarosyida.githubuser.FavAddUpdateActivity.Companion.RESULT_ADD
import com.dicoding.auliarosyida.githubuser.FavoritePageActivity
import com.dicoding.auliarosyida.githubuser.R
import com.dicoding.auliarosyida.githubuser.adapter.FavoriteAdapter
import com.dicoding.auliarosyida.githubuser.databinding.ActivityFavoritePageBinding
import com.dicoding.auliarosyida.githubuser.databinding.FragmentProfileBinding
import com.dicoding.auliarosyida.githubuser.db.UserDbContract
import com.dicoding.auliarosyida.githubuser.db.UserGithubHelper
import com.dicoding.auliarosyida.githubuser.entity.User
import com.dicoding.auliarosyida.githubuser.helper.MappingHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loopj.android.http.AsyncHttpClient.log
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

        var statusFav = user.isFavorited

        //untuk cek sudah ada dalam db atau belum
        GlobalScope.launch(Dispatchers.Main) {
            val thisContext = requireContext()
            userGithubHelper = UserGithubHelper.getInstance(thisContext)
            userGithubHelper.open()

            try {
                val deferredNotes = async(Dispatchers.IO) {
                    val cursor = userGithubHelper.queryByUname(uname)
                    MappingHelper.mapCursorToArrayList(cursor)
                    // cursor.close();
                }
                val deferredNotes2 = async(Dispatchers.IO) {
                    val cursor = userGithubHelper.queryAll()
                    MappingHelper.mapCursorToArrayList(cursor)
                    // cursor.close();
                }

                val favorites = deferredNotes.await()
                if (favorites.size > 0) {
                    println("profile fragment : dia udah favorit isinya ${favorites.size}")
                    user.isFavorited = true
                    statusFav = true
                    binding.favBtn.setColorFilter(Color.MAGENTA)
                }

                val favoritesAll = deferredNotes2.await()
                if (favoritesAll.size > 0) {
                    println("profile fragment : ada isinya ${favoritesAll.size}")
                } else {
                    println("profile fragment : kosongannnnnnn")
                }
            }finally {
                userGithubHelper.close()
            }
        }

//        if(statusFav || user.isFavorited) binding.favBtn.setColorFilter(Color.MAGENTA)

        binding.favBtn.setOnClickListener {
            statusFav = !statusFav
            setStatusFav(statusFav)
            user.isFavorited = statusFav
            println("ISFAV USER : $statusFav")
        }
    }

    

    override fun onDestroyView() {
        // Do not store the binding instance in a field, if not required.
//        userGithubHelper.close()
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
                    println("USER ADAPTER : STATUS ON")
                    binding.favBtn.setColorFilter(Color.MAGENTA)

                    var valuesTemp = valueFavoriteUser()
                    val insertResult = userGithubHelper.insert(valuesTemp)
                    if (insertResult > 0) {
                        Toast.makeText(thisContext, "Add to Favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(thisContext, "Failed to Favorite", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    println("USER ADAPTER : STATUS OFF")
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

//    fun requestAdd(){
//        adapter.addItem(note)
//        binding.rvNotes.smoothScrollToPosition(adapter.itemCount - 1)
//
//        showSnackbarMessage("Satu item berhasil ditambahkan")
//    }

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
         println("USER INSERT : masuk ke valueFavoriteUser")
         return values

 //        userGithubHelper.insert(values)
     }
}
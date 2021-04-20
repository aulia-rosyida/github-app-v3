package com.dicoding.auliarosyida.consumerapp.activity

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.consumerapp.adapter.FavoriteAdapter
import com.dicoding.auliarosyida.consumerapp.databinding.ActivityMainBinding
import com.dicoding.auliarosyida.consumerapp.db.UserDbContract.UserDbColumns.Companion.CONTENT_URI
import com.dicoding.auliarosyida.consumerapp.entity.User
import com.dicoding.auliarosyida.consumerapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var adapterFavPage: FavoriteAdapter
    private var dummyFavorite = User(0,"You do not have favorited user","", "", "","","","","")

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.setHasFixedSize(true)
        adapterFavPage = FavoriteAdapter(this)
        binding.rvList.adapter = adapterFavPage

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoritesAsync()
        } else {
            savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)?.also { adapterFavPage.listFavorites = it }
        }
//        binding.rvFavorites.adapter?.notifyDataSetChanged()

    }


    private fun loadFavoritesAsync() {
        binding.progressBar.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {

            try {
                val deferredFavorites = async(Dispatchers.IO) {
                    // CONTENT_URI = content://com.dicoding.auliarosyida.githubuser/user
                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                    MappingHelper.mapCursorToArrayList(cursor)
                }

                val favorites = deferredFavorites.await()
                if (favorites.size > 0) {
                    adapterFavPage.listFavorites = favorites
                } else {
                    var listTemp = ArrayList<User>()
                    listTemp.add(dummyFavorite)
                    adapterFavPage.listFavorites = listTemp
                    showSnackbarMessage("Tidak ada data saat ini")
                }
                binding.progressBar.visibility = View.INVISIBLE
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvList, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapterFavPage.listFavorites)
    }
}
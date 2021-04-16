package com.dicoding.auliarosyida.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.githubuser.adapter.FavoriteAdapter
import com.dicoding.auliarosyida.githubuser.databinding.ActivityFavoritePageBinding
import com.dicoding.auliarosyida.githubuser.databinding.ActivityMainBinding
import com.dicoding.auliarosyida.githubuser.db.UserGithubHelper
import com.dicoding.auliarosyida.githubuser.entity.User
import com.dicoding.auliarosyida.githubuser.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritePageActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoritePageBinding

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvFavorites.adapter = adapter

        if (savedInstanceState == null) {
            // proses ambil data
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorites = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorites)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressbarFavpage.visibility = View.VISIBLE
            val userGithubHelper = UserGithubHelper.getInstance(applicationContext)
            userGithubHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userGithubHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressbarFavpage.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.listFavorites = notes
            } else {
                adapter.listFavorites = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
            userGithubHelper.close()
        }
    }
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorites, message, Snackbar.LENGTH_SHORT).show()
    }
}
package com.dicoding.auliarosyida.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.githubuser.adapter.FavoriteAdapter
import com.dicoding.auliarosyida.githubuser.databinding.ActivityFavoritePageBinding
import com.dicoding.auliarosyida.githubuser.db.UserGithubHelper
import com.dicoding.auliarosyida.githubuser.entity.User
import com.dicoding.auliarosyida.githubuser.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritePageBinding
    lateinit var adapterFavPage: FavoriteAdapter
    private var dummyFavorite = User(0,"You do not have favorited user","", "", "","","","","")

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

        adapterFavPage = FavoriteAdapter(this)

        // proses ambil data
        loadNotesAsync()
        binding.rvFavorites.adapter = adapterFavPage
        binding.rvFavorites.adapter?.notifyDataSetChanged()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {

        // proses ambil data
        loadNotesAsync()
        super.onResume()
    }

    // function to the button on press
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun loadNotesAsync() {
        binding.progressbarFavpage.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            val userGithubHelper = UserGithubHelper.getInstance(applicationContext)
            userGithubHelper.open()

            try {
                val deferredFavorites = async(Dispatchers.IO) {
                    val cursor = userGithubHelper.queryAll()
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val favorites = deferredFavorites.await()
                if (favorites.size > 0) {
                    adapterFavPage.listFavorites = favorites
                    binding.progressbarFavpage.visibility = View.INVISIBLE
                    binding.rvFavorites.adapter?.notifyDataSetChanged()
                } else {
                    var listTemp = ArrayList<User>()
                    listTemp.add(dummyFavorite)
                    binding.progressbarFavpage.visibility = View.INVISIBLE
                    adapterFavPage.listFavorites = listTemp
                    binding.rvFavorites.adapter?.notifyDataSetChanged()
                    showSnackbarMessage("Tidak ada data saat ini")
                }
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                userGithubHelper.close()
            }
        }
    }
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorites, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                FavAddUpdateActivity.RESULT_DELETE -> {
                    val position = data.getIntExtra(FavAddUpdateActivity.EXTRA_POSITION, 0)
                    adapterFavPage.removeItem(position)
                    showSnackbarMessage("Satu item berhasil dihapus")
                }
            }
        }
    }
}
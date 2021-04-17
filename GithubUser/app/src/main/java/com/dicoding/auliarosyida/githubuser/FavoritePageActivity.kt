package com.dicoding.auliarosyida.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.MenuItem
import android.view.View
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

//         if (savedInstanceState == null) {
//             // proses ambil data
//             loadNotesAsync()
//         } else {
//             val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
//             if (list != null) {
//                 adapterFavPage.listFavorites = list
//             }
//         }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        Log.d("ACU DI FAV", "Activity A: onStart() ")
    }

    override fun onResume() {

        // proses ambil data
        loadNotesAsync()
        super.onResume()
        Log.d("ACU DI FAV", "Activity A: onResume() ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ACU DI FAV", "Activity A: onPause() ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("ACU DI FAV", "Activity A: onRestart() ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ACU DI FAV", "Activity A: onStop() ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ACU DI FAV", "Activity A: onDestroy() ")
    }
//     override fun onSaveInstanceState(outState: Bundle) {
//         super.onSaveInstanceState(outState)
//         outState.putParcelableArrayList(EXTRA_STATE, adapterFavPage.listFavorites)
//     }

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
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = userGithubHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
//                cursor.close();
            }
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                println("favorite page : ada isinya ${favorites.size}")
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
            userGithubHelper.close()
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
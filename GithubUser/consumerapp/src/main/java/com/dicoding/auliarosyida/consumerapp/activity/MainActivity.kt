package com.dicoding.auliarosyida.consumerapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.consumerapp.R
import com.dicoding.auliarosyida.consumerapp.adapter.UserAdapter
import com.dicoding.auliarosyida.consumerapp.databinding.ActivityMainBinding
import com.dicoding.auliarosyida.consumerapp.entity.User
import com.dicoding.auliarosyida.consumerapp.BuildConfig
import com.dicoding.auliarosyida.consumerapp.R
import com.dicoding.auliarosyida.consumerapp.adapter.UserAdapter
import com.dicoding.auliarosyida.consumerapp.databinding.ActivityMainBinding
import com.dicoding.auliarosyida.consumerapp.entity.User
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    private var title: String = "Consumer User List"
    private var tempSearch = "aulia-"

    private lateinit var binding: ActivityMainBinding
    private var users = mutableListOf<User>()

    private var listUserAdapter = UserAdapter(users)
    var dummyUser = User(0,"Please try with another username","Sorry, this username could not been find", "", "","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBarTitle(title)

        binding.rvList.setHasFixedSize(true)
        binding.rvList.adapter = listUserAdapter
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.progressBar.visibility = View.VISIBLE

        getUsersApi()

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                getDetailUserApi(data, data.id)
            }
        })

        binding.searchButton.setOnClickListener {
            tempSearch = binding.textInputSearch.text.toString()
            getUsersApi()
            Toast.makeText(this@MainActivity, "You search $tempSearch.", Toast.LENGTH_SHORT).show()
        }

        binding.textInputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                tempSearch = binding.textInputSearch.text.toString()
                getUsersApi()
                Toast.makeText(this@MainActivity, "You search $tempSearch.", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
       val inflater = menuInflater
       inflater.inflate(R.menu.option_menu, menu)
       return super.onCreateOptionsMenu(menu)
   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_change_settings -> {
                val mIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(mIntent)
                return true
            }
            R.id.favPage -> {
                val favIntent = Intent(this@MainActivity, FavoritePageActivity::class.java)
                startActivity(favIntent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUsersApi() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
       client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
       client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$tempSearch"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE
                // Parsing JSON
                val result = String(responseBody)
                getListUsers(result)

            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getListUsers(response: String) {
        val listUser = ArrayList<User>()

        try{
            val responseObject = JSONObject(response)
            val dataArray = responseObject.getJSONArray("items")

            val gson = Gson()
            for(i in 0 until dataArray.length()){
                val dataObject = dataArray.getJSONObject(i)
                val data = gson.fromJson(dataObject.toString(), User::class.java)
                data.id = i
                listUser.add(data)
            }

            if(listUser.size == 0){
                users.clear()
                users.add(dummyUser)
            }
            else if(users.size == 0 )users.addAll(listUser)
            else{
                users.clear()
                users.addAll(listUser)
            }
            showRecyclerList(users)
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
    private fun showRecyclerList(usersTemp: MutableList<User>) {
        listUserAdapter = UserAdapter(usersTemp)
        binding.rvList.adapter?.notifyDataSetChanged();
    }


    private fun getDetailUserApi(aUser: User, position: Int) {
        val clientDetail = AsyncHttpClient()
       clientDetail.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
       clientDetail.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${aUser.username}"

        clientDetail.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Parsing JSON
                val resultDetail = String(responseBody)

                val gson = Gson()
                val dataObject = JSONObject(resultDetail)
                val newUser = gson.fromJson(dataObject.toString(), User::class.java)
                newUser.id = position
                showSelectedUser(newUser)
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val moveWithObjectIntent = Intent(this@MainActivity, TabLayoutActivity::class.java)
        moveWithObjectIntent.putExtra(TabLayoutActivity.EXTRA_USER, user)
        startActivity(moveWithObjectIntent)
    }
}
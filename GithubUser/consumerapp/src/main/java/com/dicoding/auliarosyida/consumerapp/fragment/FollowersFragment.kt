package com.dicoding.auliarosyida.consumerapp.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.auliarosyida.consumerapp.BuildConfig
import com.dicoding.auliarosyida.consumerapp.R
import com.dicoding.auliarosyida.consumerapp.adapter.UserAdapter
import com.dicoding.auliarosyida.consumerapp.databinding.FragmentFollowersBinding
import com.dicoding.auliarosyida.consumerapp.entity.User
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.*

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding: FragmentFollowersBinding
        get() = requireNotNull(_binding)

    private var users = mutableListOf<User>()
    private var listUserAdapter = UserAdapter(users)
    private var dummyFollower = User(0,"This user has 0 of Follower","", "", "","","","","")
    private var dummyFollowing = User(0,"This user has 0 of Following","", "", "","","","","")

    var tab = ""

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(index: Int, uname: String) =
                FollowersFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, index)
                        putString(ARG_USERNAME, uname)
                    }
                }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.adapter = listUserAdapter
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.progressBarFollowers.visibility = View.VISIBLE

        getListApi()
    }

    private fun getListApi() {
        binding.progressBarFollowers.visibility = View.VISIBLE
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val uname = arguments?.getString(ARG_USERNAME)

        val client = AsyncHttpClient()
        if(index == 1) tab = "followers"
        else if(index == 2) tab = "following"

        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${uname}/${tab}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBarFollowers.visibility = View.INVISIBLE
                // Parsing JSON
                val result = String(responseBody)
                getListUsers(result)

            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBarFollowers.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getListUsers(response: String) {
        val listUser = ArrayList<User>()
        var dummyUser = dummyFollower
        if(tab == "following") dummyUser = dummyFollowing

        try{
            val dataArray = JSONArray(response)

            val gson = Gson()
            for(i in 0 until dataArray.length()){
                val dataObject = dataArray.getJSONObject(i)
                val data = gson.fromJson(dataObject.toString(), User::class.java)
                listUser.add(data)
            }

            if(listUser.size == 0){
                users.clear()
                users.add(dummyUser)
            }
            else if(users.size == 0 ) users.addAll(listUser)
            else{
                users.clear()
                users.addAll(listUser)
            }
            showRecyclerList(users)
        } catch (e: Exception) {
            makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun showRecyclerList(usersTemp: MutableList<User>) {
        listUserAdapter = UserAdapter(usersTemp)
        binding.rvFollowers.adapter?.notifyDataSetChanged();
    }

}
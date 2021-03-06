package com.dicoding.auliarosyida.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dicoding.auliarosyida.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.auliarosyida.githubuser.databinding.ActivityTabLayoutBinding
import com.dicoding.auliarosyida.githubuser.entity.User
import com.google.android.material.tabs.TabLayoutMediator

class FavAddUpdateActivity : AppCompatActivity(){
    private var title: String = "User Favorite Detail"
    private var user: User? = null
    private var position: Int = 0
    private lateinit var binding: ActivityTabLayoutBinding
    private var dummyFavorite = User(0,"You do not have any favorite user","", "", "","","","","")

    companion object {
        const val EXTRA_FAVORITE_USER = "extra_favorite_user"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_UPDATE = 200
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra(EXTRA_FAVORITE_USER)
        if (user != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        } else {
            user = dummyFavorite
        }

        //untuk menghubungkan SectionsPagerAdapter dengan ViewPager2
        val sectionsPagerAdapter = SectionsPagerAdapter(this, user!!)
        binding.viewPager.adapter = sectionsPagerAdapter

        //menghubungkan ViewPager2 dengan TabLayout dengan menggunakan TabLayoutMediator
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TabLayoutActivity.TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        setActionBarTitle(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
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
}
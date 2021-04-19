package com.dicoding.auliarosyida.consumerapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.auliarosyida.consumerapp.R
import com.dicoding.auliarosyida.consumerapp.fragment.SettingPreferenceFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.activity_settings, SettingPreferenceFragment()).commit()
    }
}
package com.dicoding.auliarosyida.githubuser.fragment

import android.R.attr
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import com.dicoding.auliarosyida.githubuser.R


class SettingPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMINDER: String
    private lateinit var CHANGE_LANG: String
    private lateinit var changeLangPreference: Preference
    private lateinit var reminderPreference: SwitchPreferenceCompat

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()

        changeLangPreference.setOnPreferenceClickListener {
            val changeLangIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(changeLangIntent)
            true
        }
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)
        CHANGE_LANG = resources.getString(R.string.key_change_language)
        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreferenceCompat
        changeLangPreference = findPreference<Preference>(CHANGE_LANG) as Preference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }


    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        val pref: Preference? = findPreference(key!!)
        println("Test SETTINGS KEY - $key?")
        when (key) {
            REMINDER -> {
                val isClicked = preferences?.getBoolean(REMINDER, false)
                println("Test SETTINGS ISCLICKED -  $isClicked?")
                when(isClicked){
                    true -> pref?.summary = getString(R.string.reminder_is_set)
                    else -> pref?.summary = getString(R.string.reminder_not_set)
                }
            }
        }
    }
}
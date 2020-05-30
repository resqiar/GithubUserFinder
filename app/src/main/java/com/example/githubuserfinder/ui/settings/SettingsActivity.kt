package com.example.githubuserfinder.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.githubuserfinder.R
import com.example.githubuserfinder.ui.settings.broadcastreceiver.ReminderNotification
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    private var sharedPref: SharedPreferences? = null
    private var sharedPrefEdit : SharedPreferences.Editor? = null
    private var reminderNotification : ReminderNotification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        /* create shared pref */
        sharedPref = getSharedPreferences("reminder", Context.MODE_PRIVATE)
        reminderNotification = ReminderNotification()

        reminderSwitch()
        setPref()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun reminderSwitch(){   // switch
        switch_notification.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefEdit = sharedPref?.edit()
            sharedPrefEdit?.putBoolean("reminder", isChecked)
            sharedPrefEdit?.apply()
            if (isChecked) {
                reminderNotification?.setReminderTime(this)
            } else {
                reminderNotification?.cancelReminder(this)
            }
        }
    }

    private fun setPref(){ // shared preferences
        val reminder = sharedPref?.getBoolean("reminder", false)

        if (reminder != null) {
            switch_notification.isChecked = reminder
        }
    }
}
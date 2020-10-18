package com.example.busticketactivity.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.busticketactivity.BuildConfig
import com.example.busticketactivity.R
import com.example.busticketactivity.signin.SignInAndRegistActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        checkFirstRun()

    }
    private fun goToSignInAndRegistActivity(){
        val intent=Intent(this@MainActivity,
            SignInAndRegistActivity::class.java)
        Handler().postDelayed({
        startActivity(intent)
        },4000)
    }
    private fun checkFirstRun() {
        val PREFS_NAME = "MyPrefsFile"
        val PREF_VERSION_CODE_KEY = "version_code"
        val DOESNT_EXIST = -1
        val currentVersionCode =
            BuildConfig.VERSION_CODE
        val prefs =
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)
        when {
            currentVersionCode == savedVersionCode -> {
                val intent=Intent(this@MainActivity,
                    SignInAndRegistActivity::class.java)
                startActivity(intent)
                finish()
            }
            savedVersionCode == DOESNT_EXIST -> {
                goToSignInAndRegistActivity()
            }


            // Update the shared preferences with the current version code
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }
}
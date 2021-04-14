package com.davoh.oauth2_twitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davoh.oauth2_twitch.di.MainApplication

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as MainApplication).getComponent().inject(this)
    }
}
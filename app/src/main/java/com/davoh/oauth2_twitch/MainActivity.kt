package com.davoh.oauth2_twitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davoh.oauth2_twitch.databinding.ActivityMainBinding
import com.davoh.oauth2_twitch.di.MainApplication
import com.davoh.oauth2_twitch.framework.TwitchOAuth2
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var twitchOAuth2: TwitchOAuth2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        (applicationContext as MainApplication).getComponent().inject(this)
    }
}
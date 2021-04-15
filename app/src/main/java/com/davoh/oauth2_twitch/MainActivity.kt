package com.davoh.oauth2_twitch

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.COLOR_SCHEME_SYSTEM
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.davoh.oauth2_twitch.databinding.ActivityMainBinding
import com.davoh.oauth2_twitch.di.MainApplication
import com.davoh.oauth2_twitch.framework.AccessToken
import com.davoh.oauth2_twitch.framework.TwitchOAuth2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        binding.btnLogin.setOnClickListener {
            val responseType = "code"
            val redirectUri = "http://localhost"

            val url = "https://id.twitch.tv/oauth2/authorize?response_type=${responseType}&client_id=1tdrqc3epx025bimv640owygxn5vaq&redirect_uri=${redirectUri}&scope=viewing_activity_read&state=c3ab8aa609ea11e793ae92361f002671"
            launchCustomWebTab(Uri.parse(url))
        }
    }

    private fun launchCustomWebTab(uri:Uri){
        val builder = CustomTabsIntent.Builder()
        val colorInt = Color.parseColor("#FF6200EE")

        val builderCustomTabColorSchemeParams = CustomTabColorSchemeParams.Builder()
        builderCustomTabColorSchemeParams.setToolbarColor(colorInt)

        builder.setDefaultColorSchemeParams(builderCustomTabColorSchemeParams.build())
        val customTabsIntent: CustomTabsIntent = builder.build()
        customTabsIntent.intent.flags = FLAG_ACTIVITY_NEW_TASK
        customTabsIntent.launchUrl(this, uri)
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if (uri != null && uri.toString().startsWith("http://localhost")) {
            val code = uri.getQueryParameter("code")
            val call = twitchOAuth2.getToken("1tdrqc3epx025bimv640owygxn5vaq",
                    "p0xsoya6rt9dfiw1p4odgw1l9cujfi",
                    code.toString(),
                    "authorization_code",
                    "http://localhost")

            call.enqueue(object : Callback<AccessToken> {
                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    binding.tvTest.text = response.body()?.accessToken
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {

                }
            })
        }
    }
}
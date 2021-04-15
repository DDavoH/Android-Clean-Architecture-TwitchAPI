package com.davoh.oauth2_twitch

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
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
            val redirectUri= "http://localhost"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://id.twitch.tv/oauth2/authorize?response_type=${responseType}&client_id=1tdrqc3epx025bimv640owygxn5vaq&redirect_uri=${redirectUri}&scope=viewing_activity_read&state=c3ab8aa609ea11e793ae92361f002671"))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if (uri!=null && uri.toString().startsWith("http://localhost")){
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
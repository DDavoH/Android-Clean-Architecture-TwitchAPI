package com.davoh.oauth2_twitch

import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.davoh.oauth2_twitch.constants.Constants
import com.davoh.oauth2_twitch.databinding.ActivityMainBinding
import com.davoh.oauth2_twitch.di.MainApplication
import com.davoh.oauth2_twitch.framework.TwitchOAuth2
import com.davoh.oauth2_twitch.framework.responses.AccessTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var twitchOAuth2:TwitchOAuth2

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        (applicationContext as MainApplication).getComponent().inject(this)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment, R.id.topGamesFragment
            )
        )
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if (uri != null && uri.toString().startsWith("http://localhost")) {
            val code = uri.getQueryParameter("code")
            Toast.makeText(this,"hola $code",Toast.LENGTH_LONG).show()
            val call = twitchOAuth2.getToken(Constants.client_id,
                Constants.client_secret,
                code.toString(),
                "authorization_code",
                "http://localhost")

            call.enqueue(object : Callback<AccessTokenResponse> {
                override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                    if(response.isSuccessful && response.body()?.accessToken!=null){
                        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                            with (sharedPref.edit()) {
                                putString(Constants.sharedPrefs_AccessToken, response.body()?.accessToken)
                                putString(Constants.sharedPrefs_RefreshToken, response.body()?.refreshToken)
                                apply()
                            }
                            navController.navigate(R.id.topGamesFragment)
                    }
                }

                override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {

                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
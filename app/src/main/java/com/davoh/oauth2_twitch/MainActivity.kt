package com.davoh.oauth2_twitch

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.davoh.oauth2_twitch.constants.Constants
import com.davoh.oauth2_twitch.databinding.ActivityMainBinding
import com.davoh.oauth2_twitch.di.MainApplication
import com.davoh.oauth2_twitch.presentation.AccessTokenViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val accessTokenViewModel: AccessTokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        (applicationContext as MainApplication).appComponent.inject(this)

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
        /*if (uri != null && uri.toString().startsWith("http://localhost")) {
            val code = uri.getQueryParameter("code")
            Toast.makeText(this,"hola $code",Toast.LENGTH_LONG).show()
            accessTokenViewModel.getAccessToken(Constants.client_id,
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
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
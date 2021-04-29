package com.davoh.oauth2_twitch

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.davoh.oauth2_twitch.constants.Constants
import com.davoh.oauth2_twitch.databinding.ActivityMainBinding
import com.davoh.oauth2_twitch.di.AccessTokenComponent
import com.davoh.oauth2_twitch.di.AccessTokenModule
import com.davoh.oauth2_twitch.di.MainApplication
import com.davoh.oauth2_twitch.presentation.AccessTokenNavigation
import com.davoh.oauth2_twitch.presentation.AccessTokenNavigation.*
import com.davoh.oauth2_twitch.presentation.AccessTokenViewModel
import com.davoh.oauth2_twitch.presentation.utils.Event
import com.davoh.oauth2_twitch.utils.getViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var accessTokenComponent: AccessTokenComponent
    private val accessTokenViewModel: AccessTokenViewModel by lazy {
        getViewModel { accessTokenComponent.accessTokenViewModel }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        accessTokenComponent = (applicationContext as MainApplication).appComponent.inject(AccessTokenModule())

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment, R.id.topGamesFragment
            )
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if (uri != null && uri.toString().startsWith("http://localhost")) {

            val code = uri.getQueryParameter("code")
            Toast.makeText(this, "hola $code", Toast.LENGTH_LONG).show()
            accessTokenViewModel.getAccessToken(
                Constants.client_id,
                Constants.client_secret,
                code.toString(),
                "authorization_code",
                "http://localhost"
            )

            accessTokenViewModel.events.observe(this, Observer(this::validateEvents))

        }
    }

    private fun validateEvents(event: Event<AccessTokenNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when(navigation) {
                is ShowAccessTokenError -> navigation.run {
                    Toast.makeText(
                        this@MainActivity,
                        "Error -> ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ShowAccessToken -> navigation.run {
                    val sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE
                    )
                    with(sharedPref.edit()) {
                        putString(Constants.sharedPrefs_AccessToken, token.accessToken)
                        putString(Constants.sharedPrefs_RefreshToken, token.refreshToken)
                        apply()
                    }
                    navController.navigate(R.id.topGamesFragment)
                }
                HideLoading -> {
                    //srwCharacterList.isRefreshing = false
                }
                ShowLoading -> {
                    //srwCharacterList.isRefreshing = true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
package com.davoh.oauth2_twitch.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.davoh.oauth2_twitch.R
import com.davoh.oauth2_twitch.databinding.FragmentLoginBinding
import com.davoh.oauth2_twitch.framework.TwitchOAuth2
import com.davoh.oauth2_twitch.framework.responses.AccessTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class LoginFragment : Fragment() {

    @Inject
    lateinit var twitchOAuth2: TwitchOAuth2

    private var _binding : FragmentLoginBinding ?= null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        customTabsIntent.launchUrl(requireContext(), uri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
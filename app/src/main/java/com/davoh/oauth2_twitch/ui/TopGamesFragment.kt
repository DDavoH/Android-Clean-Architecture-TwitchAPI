package com.davoh.oauth2_twitch.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.davoh.oauth2_twitch.R
import com.davoh.oauth2_twitch.constants.Constants
import com.davoh.oauth2_twitch.databinding.FragmentTopGamesBinding
import com.davoh.oauth2_twitch.di.MainApplication
import com.davoh.oauth2_twitch.framework.TwitchAPI
import com.davoh.oauth2_twitch.framework.responses.TopGamesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class TopGamesFragment : Fragment() {
    private var _binding : FragmentTopGamesBinding?=null
    private val binding get() = _binding!!

    @Inject
    lateinit var twitchAPI: TwitchAPI

    override fun onStart() {
        super.onStart()
        val sharedPref = requireContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()
        val refreshToken = sharedPref.getString(Constants.sharedPrefs_RefreshToken, "").toString()
        if(accessToken.isEmpty() || refreshToken.isEmpty()){
            findNavController().navigate(R.id.action_topGamesFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopGamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().applicationContext as MainApplication).getComponent().injectContext(this)
        binding.btnLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_topGamesFragment_to_loginFragment)
        }
        val sharedPref = requireContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()
        val refreshToken = sharedPref.getString(Constants.sharedPrefs_RefreshToken, "").toString()
        val call = twitchAPI.getTopGames("Bearer $accessToken", Constants.client_id)
        call.enqueue(object: Callback<TopGamesResponse> {
            override fun onResponse(
                call: Call<TopGamesResponse>,
                response: Response<TopGamesResponse>
            ) {
                        Toast.makeText(requireContext(), response.body()?.gameList.toString(),Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<TopGamesResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "failure",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.davoh.oauth2_twitch.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davoh.oauth2_twitch.R
import com.davoh.oauth2_twitch.adapters.TopGamesAdapter
import com.davoh.oauth2_twitch.constants.Constants
import com.davoh.oauth2_twitch.databinding.FragmentTopGamesBinding
import com.davoh.oauth2_twitch.di.MainApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class TopGamesFragment : Fragment() {
    private var _binding : FragmentTopGamesBinding?=null
    private val binding get() = _binding!!


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


        //RecyclerView
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rv.layoutManager = layoutManager
        val adapter = TopGamesAdapter()
        binding.rv.adapter = adapter
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (visibleItemCount + lastVisibleItem + 5 >= totalItemCount) {
                    val sharedPref = requireContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()
                    val refreshToken = sharedPref.getString(Constants.sharedPrefs_RefreshToken, "").toString()
                    //val newcall = twitchAPI.getTopGames("Bearer $accessToken", Constants.client_id, )
                }
            }
        })

        binding.btnLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_topGamesFragment_to_loginFragment)
        }
        val sharedPref = requireContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()
        val refreshToken = sharedPref.getString(Constants.sharedPrefs_RefreshToken, "").toString()
        /*val call = twitchAPI.getTopGames("Bearer $accessToken", Constants.client_id)
        call.enqueue(object: Callback<TopGamesResponse> {
            override fun onResponse(
                call: Call<TopGamesResponse>,
                response: Response<TopGamesResponse>
            ) {
                val gameList = response.body()?.gameList?.toDomainGameList()
                adapter.submitList(gameList)
            }

            override fun onFailure(call: Call<TopGamesResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "failure",Toast.LENGTH_LONG).show()
            }
        })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
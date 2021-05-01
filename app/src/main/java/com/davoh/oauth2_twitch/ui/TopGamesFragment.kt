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
import com.davoh.oauth2_twitch.di.*
import com.davoh.oauth2_twitch.presentation.TopGamesListViewModel
import com.davoh.oauth2_twitch.presentation.TopGamesListViewModel.TopGamesNavigation
import com.davoh.oauth2_twitch.presentation.TopGamesListViewModel.TopGamesNavigation.*
import com.davoh.oauth2_twitch.utils.getViewModel
import androidx.lifecycle.Observer
import com.davoh.oauth2_twitch.presentation.utils.Event



class TopGamesFragment : Fragment() {
    private var _binding : FragmentTopGamesBinding?=null
    private val binding get() = _binding!!

    private lateinit var topGamesComponent: TopGamesComponent
    private val topGamesListViewModel: TopGamesListViewModel by lazy {
        getViewModel { topGamesComponent.topGamesListViewModel }
    }

    private val adapter = TopGamesAdapter()

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

        topGamesComponent = (requireActivity().applicationContext as MainApplication).appComponent.inject(TopGamesModule())

        //RecyclerView
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rv.layoutManager = layoutManager
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

        topGamesListViewModel.getTopGames("Bearer $accessToken", Constants.client_id, "")
        topGamesListViewModel.events.observe(viewLifecycleOwner, Observer(this::validateEvents))
    }

    private fun validateEvents(event: Event<TopGamesNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when(navigation){
                is ShowTopGamesError->navigation.run{
                    Toast.makeText(
                        requireContext(),
                        "Error -> ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ShowTopGamesList -> navigation.run{
                    adapter.submitList(gameList)
                }
                HideLoading -> {

                }
                ShowLoading -> {

                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
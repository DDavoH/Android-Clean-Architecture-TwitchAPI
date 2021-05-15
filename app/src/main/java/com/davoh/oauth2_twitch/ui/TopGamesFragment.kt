package com.davoh.oauth2_twitch.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.davoh.oauth2_twitch.domain.Game
import com.davoh.oauth2_twitch.presentation.FavoriteGamesViewModel
import com.davoh.oauth2_twitch.presentation.FavoriteGamesViewModel.FavoriteGamesNavigation
import com.davoh.oauth2_twitch.presentation.FavoriteGamesViewModel.FavoriteGamesNavigation.*
import com.davoh.oauth2_twitch.presentation.RefreshAccessTokenViewModel
import com.davoh.oauth2_twitch.presentation.RefreshAccessTokenViewModel.RefreshAccessTokenNavigation
import com.davoh.oauth2_twitch.presentation.RefreshAccessTokenViewModel.RefreshAccessTokenNavigation.*
import com.davoh.oauth2_twitch.presentation.RevokeAccessTokenViewModel
import com.davoh.oauth2_twitch.presentation.RevokeAccessTokenViewModel.RevokeAccessTokenNavigation
import com.davoh.oauth2_twitch.presentation.RevokeAccessTokenViewModel.RevokeAccessTokenNavigation.*
import com.davoh.oauth2_twitch.presentation.utils.Event
import kotlinx.coroutines.launch
import com.davoh.oauth2_twitch.utils.getViewModel

class TopGamesFragment : Fragment() {

    private var _binding: FragmentTopGamesBinding? = null
    private val binding get() = _binding!!

    private lateinit var topGamesComponent: TopGamesComponent
    private val topGamesListViewModel: TopGamesListViewModel by lazy {
        getViewModel { topGamesComponent.topGamesListViewModel }
    }
    private val favoriteGamesViewModel: FavoriteGamesViewModel by lazy {
        getViewModel { topGamesComponent.favoriteGamesViewModel }
    }

    private lateinit var accessTokenComponent: AccessTokenComponent
    private val revokeAccessTokenViewModel: RevokeAccessTokenViewModel by lazy {
        getViewModel { accessTokenComponent.revokeAccessTokenViewModel }
    }
    private val refreshAccessTokenViewModel: RefreshAccessTokenViewModel by lazy {
        getViewModel { accessTokenComponent.refreshAccessTokenViewModel }
    }

    private val adapter = TopGamesAdapter()
    private val favoriteGamesAdapter = TopGamesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = requireContext().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()
        val refreshToken = sharedPref.getString(Constants.sharedPrefs_RefreshToken, "").toString()
        if (accessToken.isEmpty() || refreshToken.isEmpty()) {
            findNavController().navigate(R.id.action_topGamesFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_top_games,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topGamesComponent =
            (requireActivity().applicationContext as MainApplication).appComponent.inject(
                TopGamesModule()
            )
        accessTokenComponent =
            (requireActivity().applicationContext as MainApplication).appComponent.inject(
                AccessTokenModule()
            )

        topGamesListViewModel.events.observe(
            viewLifecycleOwner,
            Observer(this::validateTopGamesEvents)
        )
        revokeAccessTokenViewModel.events.observe(
            viewLifecycleOwner,
            Observer(this::validateRevokeAccessTokenEvents)
        )
        refreshAccessTokenViewModel.events.observe(
            viewLifecycleOwner,
            Observer(this::validateRefreshAccessTokenEvents)
        )
        favoriteGamesViewModel.events.observe(
            viewLifecycleOwner,
            Observer(this::validateFavoriteGamesEvents)
        )
        favoriteGamesViewModel.favoriteGamesList.observe(
            viewLifecycleOwner,
            Observer(favoriteGamesViewModel::onFavoriteGameList)
        )

        topGamesRecyclerView()
        favoriteGamesRecyclerView()

        binding.swipeRefreshRv.setOnRefreshListener {
            val sharedPref = requireContext().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
            val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()

            topGamesListViewModel.onRetryGetAllCharacter(
                binding.rv.adapter?.itemCount ?: 0,
                "Bearer $accessToken",
                Constants.client_id,
                "",
                ""
            )
        }

        binding.btnLogOut.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
            val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()
            revokeAccessTokenViewModel.revokeAccessToken(Constants.client_id, accessToken)
        }
        val sharedPref = requireContext().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val accessToken = sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()

        topGamesListViewModel.getTopGames("Bearer $accessToken", Constants.client_id, "", "")
    }

    private fun topGamesRecyclerView() {
        //RecyclerView
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = adapter
        adapter.setOnItemClickListener(object : TopGamesAdapter.OnItemClickListener {
            override fun onFavoriteBtnClick(game: Game) {
                favoriteGamesViewModel.updateFavoriteGameStatus(game)
            }
        })

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                val sharedPref = requireContext().getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
                val accessToken =
                    sharedPref.getString(Constants.sharedPrefs_AccessToken, "").toString()
                val cursor =
                    sharedPref.getString(Constants.sharedPrefs_paginationCursor, "").toString()

                topGamesListViewModel.onLoadMoreItems(
                    visibleItemCount,
                    firstVisibleItemPosition,
                    totalItemCount,
                    "Bearer $accessToken",
                    Constants.client_id,
                    cursor,
                    ""
                )
            }
        })
    }

    private fun favoriteGamesRecyclerView() {
        val layoutManagerFavoriteGames =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFavoriteGames.layoutManager = layoutManagerFavoriteGames
        binding.rvFavoriteGames.adapter = favoriteGamesAdapter
        favoriteGamesAdapter.setOnItemClickListener(object : TopGamesAdapter.OnItemClickListener {
            override fun onFavoriteBtnClick(game: Game) {
                favoriteGamesViewModel.updateFavoriteGameStatus(game)
            }
        })
    }

    private fun validateTopGamesEvents(event: Event<TopGamesNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowTopGamesError -> navigation.run {
                    Toast.makeText(
                        requireContext(),
                        "Error showTopGamesError-> ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val sharedPref = requireContext().getSharedPreferences(
                        getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE
                    )
                    val refreshToken =
                        sharedPref.getString(Constants.sharedPrefs_RefreshToken, "").toString()
                    refreshAccessTokenViewModel.refreshToken(
                        "refresh_token",
                        refreshToken,
                        Constants.client_id,
                        Constants.client_secret
                    )
                }
                is ShowTopGamesList -> navigation.run {
                    lifecycleScope.launch {
                        val listGames = adapter.currentList.toMutableList()
                        listGames.addAll(topGames.gameList)
                        adapter.submitList(listGames)
                        val sharedPref = requireContext().getSharedPreferences(
                            getString(R.string.preference_file_key),
                            Context.MODE_PRIVATE
                        )
                        sharedPref.edit {
                            putString(
                                Constants.sharedPrefs_paginationCursor,
                                topGames.pagination.cursor
                            )
                            apply()
                        }
                    }
                }
                TopGamesNavigation.HideLoading -> {
                    binding.swipeRefreshRv.isRefreshing = false
                }
                TopGamesNavigation.ShowLoading -> {
                    binding.swipeRefreshRv.isRefreshing = true
                }
            }

        }
    }

    private fun validateRevokeAccessTokenEvents(event: Event<RevokeAccessTokenNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowRevokeAccessTokenError -> navigation.run {
                    Toast.makeText(
                        requireContext(),
                        "Error ShowRevokeAccessTokenError-> ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ShowRevokeAccessToken -> {
                    val sharedPref = requireContext().getSharedPreferences(
                        getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE
                    )
                    sharedPref.edit {
                        putString(Constants.sharedPrefs_AccessToken, "")
                        putString(Constants.sharedPrefs_RefreshToken, "")
                        putString(Constants.sharedPrefs_paginationCursor, "")
                        commit()
                    }
                    findNavController().navigate(R.id.action_topGamesFragment_to_loginFragment)
                }
                is RevokeAccessTokenNavigation.HideLoading -> {
                    binding.swipeRefreshRv.isRefreshing = false
                }
                is RevokeAccessTokenNavigation.ShowLoading -> {
                    binding.swipeRefreshRv.isRefreshing = true
                }
            }
        }
    }

    private fun validateRefreshAccessTokenEvents(event: Event<RefreshAccessTokenNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowRefreshAccessTokenError -> navigation.run {
                    Toast.makeText(
                        requireContext(),
                        "Error Refreshing token-> ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ShowRefreshAccessToken -> navigation.run {
                    val sharedPref = requireContext().getSharedPreferences(
                        getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE
                    )
                    sharedPref.edit {
                        putString(Constants.sharedPrefs_AccessToken, token.accessToken)
                        putString(Constants.sharedPrefs_RefreshToken, token.refreshToken)
                        putString(Constants.sharedPrefs_paginationCursor, "")
                        apply()
                    }
                }
                is RefreshAccessTokenNavigation.HideLoading -> {

                }
                is RefreshAccessTokenNavigation.ShowLoading -> {

                }
            }
        }
    }

    private fun validateFavoriteGamesEvents(event: Event<FavoriteGamesNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowEmptyGameListMessage -> {

                }
                is ShowFavoriteGameList -> navigation.run {
                    favoriteGamesAdapter.submitList(favoriteGames)
                }
                is UpdatedFavoriteGame -> {

                }
                is UpdateFavoriteGameError -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
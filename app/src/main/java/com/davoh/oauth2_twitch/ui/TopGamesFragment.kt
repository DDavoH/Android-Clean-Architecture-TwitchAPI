package com.davoh.oauth2_twitch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.davoh.oauth2_twitch.R
import com.davoh.oauth2_twitch.databinding.FragmentTopGamesBinding


class TopGamesFragment : Fragment() {
    private var _binding : FragmentTopGamesBinding?=null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val user="here"
        if(user==null){
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
        binding.btnLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_topGamesFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
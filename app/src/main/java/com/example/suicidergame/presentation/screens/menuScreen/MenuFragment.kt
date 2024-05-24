package com.example.suicidergame.presentation.screens.menuScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.suicidergame.R
import com.example.suicidergame.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            buttonStartGame.setOnClickListener {
                findNavController().navigate(R.id.gameFragment)
            }
            buttonResults.setOnClickListener {
                findNavController().navigate(R.id.resultsFragment)
            }
            buttonStore.setOnClickListener {
                findNavController().navigate(R.id.storeFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
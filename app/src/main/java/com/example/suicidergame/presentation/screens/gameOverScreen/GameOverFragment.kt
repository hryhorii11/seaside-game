package com.example.suicidergame.presentation.screens.gameOverScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.suicidergame.R
import com.example.suicidergame.databinding.FragmentGameOverBinding
import com.example.suicidergame.presentation.utils.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameOverFragment : Fragment() {
    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameOverViewModel by viewModels()
    private val args: GameOverFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameOverBinding.inflate(inflater, container, false)
        setListeners()
        setObservers()
        setContent()
        return binding.root
    }

    private fun setObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bestScore.collect {
                    when (it) {
                        is UIState.Error -> {
                            binding.textViewBestScoreLabel.visibility = View.GONE
                        }

                        is UIState.Init -> {}
                        is UIState.Success -> {
                            binding.textViewBestScore.text = it.data.toString()
                        }
                    }
                }
            }
        }
    }

    private fun setContent() {
        binding.textViewScore.text = args.currentScore.toString()
        viewModel.setBestScore()
    }

    private fun setListeners() {
        with(binding) {
            buttonRestart.setOnClickListener {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.menuFragment, false)
                    .build()
                findNavController().navigate(R.id.gameFragment, null, navOptions)
            }

            buttonMenu.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
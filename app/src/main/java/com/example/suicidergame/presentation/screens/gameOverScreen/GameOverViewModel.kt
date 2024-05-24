package com.example.suicidergame.presentation.screens.gameOverScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suicidergame.data.repository.ScoreRepository
import com.example.suicidergame.presentation.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameOverViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _bestScore = MutableStateFlow<UIState<Int>>(UIState.Init())
    val bestScore: StateFlow<UIState<Int>> = _bestScore

    fun setBestScore() {
        viewModelScope.launch {
            scoreRepository.getBestScore().collect {
                if (it.isSuccess) _bestScore.value = UIState.Success(it.getOrNull()!!.score)
                else _bestScore.value =
                    UIState.Error(it.exceptionOrNull()?.message ?: "Unknown message")
            }
        }
    }

}
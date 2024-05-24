package com.example.suicidergame.presentation.screens.resultsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suicidergame.data.model.Score
import com.example.suicidergame.data.repository.ScoreRepository
import com.example.suicidergame.presentation.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _results = MutableStateFlow<UIState<List<Score>>>(UIState.Init())
    val results: StateFlow<UIState<List<Score>>> = _results

    fun setResults() {
        viewModelScope.launch {
            scoreRepository.getScores().collect {
                if (it.isSuccess) _results.value = UIState.Success(it.getOrNull()!!)
                else _results.value =
                    UIState.Error(it.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

}
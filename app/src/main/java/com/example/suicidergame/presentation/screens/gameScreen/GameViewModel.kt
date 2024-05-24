package com.example.suicidergame.presentation.screens.gameScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suicidergame.data.model.Score
import com.example.suicidergame.data.preferences.PreferencesManager
import com.example.suicidergame.data.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun saveScore(score: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            scoreRepository.addScore(
                Score(
                    0,
                    score,
                    Date()
                )
            )
        }
    }

    fun getChosenLifePreserver(): Int {
        return preferencesManager.getChosenLifePreserver()
    }

    fun addEarnedMoney(moneyAmount: Int) {
        preferencesManager.saveUserMoney(moneyAmount)
    }

    fun getUserMoney(): Int = preferencesManager.getUserMoney()
}
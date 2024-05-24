package com.example.suicidergame.presentation.screens.storeFragment

import androidx.lifecycle.ViewModel
import com.example.suicidergame.data.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun saveChosenLifePreserver(id: Int) {
        preferencesManager.saveChosenLifePreserver(id)
    }

    fun getChosenLifePreserver(): Int {
        return preferencesManager.getChosenLifePreserver()
    }

    fun getUserMoneyAmount(): Int {
        return preferencesManager.getUserMoney()
    }

    fun getBoughtLifePreserves(): List<Int> {
        return preferencesManager.getBoughtLifePreservers()
    }

    fun saveBoughtPreserves(boughtItems: List<Int>) {
        preferencesManager.saveBoughtLifePreservers(boughtItems)
    }

    fun updateMoneyAmount(moneyAmount: Int) {
        preferencesManager.saveUserMoney(moneyAmount)
    }
}
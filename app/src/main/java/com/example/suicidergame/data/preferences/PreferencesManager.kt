package com.example.suicidergame.data.preferences

interface PreferencesManager {

    fun saveChosenLifePreserver(id: Int)
    fun getChosenLifePreserver(): Int

    fun saveBoughtLifePreservers(preserversIndexes: List<Int>)
    fun getBoughtLifePreservers(): List<Int>

    fun saveUserMoney(moneyAmount: Int)
    fun getUserMoney(): Int
}
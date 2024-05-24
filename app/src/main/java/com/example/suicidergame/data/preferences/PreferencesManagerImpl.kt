package com.example.suicidergame.data.preferences

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class PreferencesManagerImpl @Inject constructor(
    private val context: Context
) : PreferencesManager {
    private val sharedPref = context.getSharedPreferences(
        "sharedPref", Context.MODE_PRIVATE
    )
    private val gson = Gson()

    private val chosenLifePreserverKey = "life_preserver"
    private val boughtLifePreservesKey = "bought_indexes"
    private val userMoneyKey = "money"

    override fun saveChosenLifePreserver(id: Int) {
        with(sharedPref.edit()) {
            putInt(chosenLifePreserverKey, id)
            apply()
        }
    }

    override fun getChosenLifePreserver(): Int {
        return sharedPref.getInt(chosenLifePreserverKey, 0)
    }

    override fun saveBoughtLifePreservers(preserversIndexes: List<Int>) {
        val json = gson.toJson(preserversIndexes)
        sharedPref.edit().putString(boughtLifePreservesKey, json).apply()
    }

    override fun getBoughtLifePreservers(): List<Int> {
        val json = sharedPref.getString(boughtLifePreservesKey, null)
        return if (json != null) {
            val type = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(json, type)
        } else {
            listOf(0)
        }
    }

    override fun saveUserMoney(moneyAmount: Int) {
        sharedPref.edit().putInt(userMoneyKey, moneyAmount).apply()
    }

    override fun getUserMoney(): Int {
        return sharedPref.getInt(userMoneyKey, 0)
    }
}


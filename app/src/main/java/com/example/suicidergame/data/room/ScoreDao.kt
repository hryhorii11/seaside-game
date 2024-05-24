package com.example.suicidergame.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert
    fun addScore(entity: ScoreEntity)

    @Query("SELECT * FROM scores WHERE score = (SELECT MAX(score) FROM scores) LIMIT 1")
    fun getBestScore(): ScoreEntity

    @Query("Select * from scores")
    fun getScores(): List<ScoreEntity>
}
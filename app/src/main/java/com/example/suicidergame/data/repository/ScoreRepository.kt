package com.example.suicidergame.data.repository

import com.example.suicidergame.data.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {

    fun addScore(score: Score)

    fun getBestScore(): Flow<Result<Score>>

    fun getScores(): Flow<Result<List<Score>>>
}
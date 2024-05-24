package com.example.suicidergame.data.repository

import com.example.suicidergame.data.model.Score
import com.example.suicidergame.data.room.ScoreDao
import com.example.suicidergame.data.room.ScoreEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ScoreRepositoryImpl @Inject constructor(
    private val scoreDao: ScoreDao
) : ScoreRepository {

    override fun addScore(score: Score) {
        scoreDao.addScore(ScoreEntity.createEntity(score))
    }

    override fun getBestScore(): Flow<Result<Score>> {
        return flow {
            emit(Result.success(scoreDao.getBestScore().toModel()))
        }.flowOn(Dispatchers.IO).catch {
            emit(Result.failure(it))
        }
    }

    override fun getScores(): Flow<Result<List<Score>>> {
        return flow {
            emit(Result.success(scoreDao.getScores().map { it.toModel() }
                .sortedByDescending { it.score }))
        }.flowOn(Dispatchers.IO).catch {
            emit(Result.failure(it))
        }
    }

}
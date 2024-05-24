package com.example.suicidergame.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.suicidergame.data.model.Score
import java.util.Date


@Entity(
    tableName = "scores"
)
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val date: Date
) {
    fun toModel() = Score(id, score, date)

    companion object {
        fun createEntity(score: Score) = ScoreEntity(score.id, score.score, score.date)
    }
}
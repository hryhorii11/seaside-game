package com.example.suicidergame.presentation.screens.resultsScreen

import androidx.recyclerview.widget.DiffUtil
import com.example.suicidergame.data.model.Score

object ResultsDiffUtil : DiffUtil.ItemCallback<Score>() {
    override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem == newItem
    }
}
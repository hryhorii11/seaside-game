package com.example.suicidergame.presentation.screens.resultsScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.suicidergame.data.model.Score
import com.example.suicidergame.databinding.ItemResultBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ResultsAdapter : ListAdapter<Score, ResultsAdapter.ResultViewHolder>(ResultsDiffUtil) {

    inner class ResultViewHolder(private val binding: ItemResultBinding) :
        ViewHolder(binding.root) {
        fun bind(score: Score) {
            with(binding) {
                textviewScore.text = score.score.toString()
                val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
                textViewDate.text = dateFormat.format(score.date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemResultBinding.inflate(layoutInflater, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
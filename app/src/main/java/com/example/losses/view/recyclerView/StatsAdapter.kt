package com.example.losses.view.recyclerView

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.losses.databinding.ContentItemBinding

class StatsAdapter : RecyclerView.Adapter<StatsAdapter.StatisticItemViewHolder>() {

    private val stats: MutableList<StatisticData> = mutableListOf()

    fun setStatistics(items: MutableList<StatisticData>) {
        stats.clear()
        stats.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticItemViewHolder {
        val binding = ContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatisticItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticItemViewHolder, position: Int) {
        holder.bind(stats[position])
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    class StatisticItemViewHolder(private val binding: ContentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(statisticData: StatisticData) {
            binding.statistics.text = statisticData.lossNumber.toString()
            binding.increase.apply {
                if(statisticData.increasedBy > 0) {
                    visibility = VISIBLE
                    text = "+ ${statisticData.increasedBy}"
                } else {
                    visibility = GONE
                }
            }
            binding.itemImage.setImageResource(statisticData.pictureResource)
            binding.itemName.text =
                binding.root.context.resources.getText(statisticData.nameResource)
        }
    }
}
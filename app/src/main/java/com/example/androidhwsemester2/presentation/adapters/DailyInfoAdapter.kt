package com.example.androidhwsemester2.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.DailyInfoItemBinding
import com.example.androidhwsemester2.domain.entity.WeatherDayInfo

class DailyInfoAdapter : RecyclerView.Adapter<DailyInfoAdapter.DailyInfoViewHolder>() {

    private var list: List<WeatherDayInfo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyInfoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_info_item, parent, false)
        return DailyInfoViewHolder(DailyInfoItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: DailyInfoViewHolder, position: Int) {
        holder.onBind(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<WeatherDayInfo>) {
        list = newList
        notifyDataSetChanged() // :(
    }

    override fun getItemCount(): Int = list.size

    inner class DailyInfoViewHolder(private val binding: DailyInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val model = list[position]
            with(binding) {
                daysBeforeTv.text =
                    binding.root.context.getString(R.string.some_day_ago, position + 1)
                temperatureTv.text =
                    binding.root.context.getString(R.string.temperature, model.temp)
                pressureTv.text =
                    binding.root.context.getString(R.string.pressure, model.pressure)
                humidityTv.text =
                    binding.root.context.getString(R.string.humidity, model.humidity)

            }
        }

    }
}

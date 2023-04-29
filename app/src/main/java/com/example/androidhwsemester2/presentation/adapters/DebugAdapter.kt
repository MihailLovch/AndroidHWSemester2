package com.example.androidhwsemester2.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.RequestAmountItemBinding

class DebugAdapter(
    private var items: List<Pair<String, Int>>
) : RecyclerView.Adapter<DebugAdapter.DebugViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.request_amount_item, parent, false)
        return DebugViewHolder(RequestAmountItemBinding.bind(view))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DebugViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class DebugViewHolder(private val binding: RequestAmountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pair: Pair<String, Int>) {
            with(binding) {
                cityNameTv.text = pair.first + " : " + pair.second
            }
        }
    }
}

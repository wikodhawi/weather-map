package com.dhabasoft.weathermap.view.detailcity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailByHourEntity
import com.dhabasoft.weathermap.databinding.ItemByHourBinding

/**
 * Created by dhaba
 */
class DetailCityByHourAdapter : RecyclerView.Adapter<DetailCityByHourAdapter.ViewHolder>(){
    private var details: List<DetailByHourEntity> = ArrayList()
    fun setListDetailByHour(details: List<DetailByHourEntity>)
    {
        this.details = details
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemByHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: DetailByHourEntity = details[position]
        holder.run {
            binding.tvHour.text = item.time
            binding.tvHumidity.text = "Humidity ${item.humidity}"
            binding.tvTemperature.text = "Temperature ${item.temperature}"
            binding.tvWindCondition.text = "Wind ${item.windCondition}"
        }
    }

    override fun getItemCount(): Int {
        return details.size
    }

    class ViewHolder(val binding: ItemByHourBinding) : RecyclerView.ViewHolder(binding.root)
}
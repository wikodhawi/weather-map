package com.dhabasoft.weathermap.view.detailcity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailCityEntity
import com.dhabasoft.weathermap.databinding.ItemByDateBinding

/**
 * Created by dhaba
 */
class DetailCityAdapter : RecyclerView.Adapter<DetailCityAdapter.ViewHolder>(){
    private var details: List<DetailCityEntity> = ArrayList()
    fun setListDetail(details: List<DetailCityEntity>)
    {
        this.details = details
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemByDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: DetailCityEntity = details[position]
        holder.run {
            binding.tvDate.text = item.dateString
            val adapter = DetailCityByHourAdapter()
            binding.rcyDetailDate.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rcyDetailDate.adapter = adapter
            adapter.setListDetailByHour(item.detailByHour)
        }
    }

    override fun getItemCount(): Int {
        return details.size
    }

    class ViewHolder(val binding: ItemByDateBinding) : RecyclerView.ViewHolder(binding.root)
}
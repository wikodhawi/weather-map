package com.dhabasoft.weathermap.view.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.databinding.ItemCityBinding
import com.dhabasoft.weathermap.utils.CountryFlags

/**
 * Created by dhaba
 */
class CityAdapter (private val onClickCallback: OnClickCallback) : RecyclerView.Adapter<CityAdapter.ViewHolder>(){
    private var cities: List<CityEntity> = ArrayList()
    fun setListCities(tvShows: List<CityEntity>)
    {
        this.cities = tvShows
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: CityEntity = cities[position]
        holder.run {
            binding.cardCity.setOnClickListener {
                onClickCallback.toDetailCity(item)
            }
            binding.tvCityName.text = item.cityName
            binding.tvCountryFlag.text = CountryFlags.getCountryFlagByCountryCode(item.countryCode)
            binding.tvCountryName.text = item.countryCode
            Glide.with(holder.itemView.context).load("http://openweathermap.org/img/wn/${item.weatherIcon}@2x.png").into(holder.binding.imageWeather)
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class ViewHolder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClickCallback {
        fun toDetailCity(cityFavouriteEntity: CityEntity)
    }
}
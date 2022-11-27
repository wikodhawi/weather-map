package com.dhabasoft.weathermap.view.detailcity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailCityEntity
import com.dhabasoft.weathermap.view.weather.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class DetailCityViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase) :
    ViewModel() {
    val isFavourite = weatherUseCase.getFlowIsFavourite().asLiveData()

    fun getIsFavourite(cityId: Int) = weatherUseCase.getIsFavoriteCity(cityId)

    fun setOrRemoveFromCityFavourite(cityEntity: CityEntity) =
        weatherUseCase.setOrRemoveFromCityFavorite(cityEntity)

    fun getDetailCity(cityId: String): LiveData<Resource<List<DetailCityEntity>>> =
        weatherUseCase.detailCity(cityId).asLiveData()
}
package com.dhabasoft.weathermap.view.detailcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.databinding.ActivityDetailCityBinding
import com.dhabasoft.weathermap.utils.CountryFlags
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCityBinding
    private var selectedCity: CityEntity? = null
    private val viewModel: DetailCityViewModel by viewModels()

    companion object {
        const val KEY_SELECTED_CITY = "selected_city"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedCity = intent.getParcelableExtra(KEY_SELECTED_CITY)
        title =
            "${selectedCity?.cityName} ${CountryFlags.getCountryFlagByCountryCode(selectedCity?.countryCode ?: "")}"

        viewModel.isFavourite.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fabFavouriteCity.setImageResource(R.drawable.ic_baseline_favorite_red)
            } else {
                binding.fabFavouriteCity.setImageResource(R.drawable.ic_baseline_favorite_white)
            }
        }

        binding.fabFavouriteCity.setOnClickListener {
            selectedCity?.let { city ->
                viewModel.setOrRemoveFromCityFavourite(city)
            }
        }

        viewModel.getIsFavourite(selectedCity?.id?: 0)
    }
}
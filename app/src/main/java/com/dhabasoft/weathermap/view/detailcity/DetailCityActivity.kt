package com.dhabasoft.weathermap.view.detailcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.databinding.ActivityDetailCityBinding
import com.dhabasoft.weathermap.utils.CountryFlags

class DetailCityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCityBinding

    companion object {
        const val KEY_SELECTED_CITY = "selected_city"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedCity = intent.getParcelableExtra<CityEntity>(KEY_SELECTED_CITY)
        title = "${selectedCity?.cityName} ${CountryFlags.getCountryFlagByCountryCode(selectedCity?.countryCode?: "")}"
    }
}
package com.dhabasoft.weathermap.view.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dhabasoft.weathermap.databinding.ActivityWeatherBinding
import com.dhabasoft.weathermap.view.stories.StoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private var binding: ActivityWeatherBinding? = null
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}
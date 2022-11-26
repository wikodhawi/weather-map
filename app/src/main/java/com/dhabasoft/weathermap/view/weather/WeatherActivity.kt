package com.dhabasoft.weathermap.view.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dhabasoft.weathermap.databinding.ActivityWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private var binding: ActivityWeatherBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}
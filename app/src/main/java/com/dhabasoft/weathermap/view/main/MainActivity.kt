package com.dhabasoft.weathermap.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dhabasoft.weathermap.databinding.ActivityMainBinding
import com.dhabasoft.weathermap.view.maps.MapsActivity
import com.dhabasoft.weathermap.view.stories.StoriesActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStories.setOnClickListener {
            val intent = Intent(applicationContext, StoriesActivity::class.java)
            startActivity(intent)
        }

        binding.btnMaps.setOnClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}
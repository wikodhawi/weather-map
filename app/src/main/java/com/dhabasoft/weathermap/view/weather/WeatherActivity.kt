package com.dhabasoft.weathermap.view.weather

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.databinding.ActivityWeatherBinding
import com.dhabasoft.weathermap.view.stories.StoriesViewModel
import com.dhabasoft.weathermap.view.weather.adapter.CityAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var adapter: CityAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.edtSearchCities.setOnTouchListener(View.OnTouchListener { v, event ->
            val drawableRight = 2
            when (event?.action) {
                MotionEvent.ACTION_UP ->
                    if (event.rawX >= (binding.edtSearchCities.right - binding.edtSearchCities.compoundDrawables[drawableRight].bounds.width())) {
                        findCity()
                        return@OnTouchListener true
                    }
            }

            v?.onTouchEvent(event) ?: true
        })

        binding.edtSearchCities.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                findCity()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.edtSearchCities.windowToken, 0)
            }
            false
        }
    }

    private fun initRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rcyMain.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
        }
        adapter = CityAdapter(object : CityAdapter.OnClickCallback {
            override fun toDetailCity(cityFavouriteEntity: CityEntity) {
//                TODO("Not yet implemented")
            }
        })
        binding.rcyMain.adapter = adapter
    }

    private fun findCity() {
        viewModel.findCity(binding.edtSearchCities.text.toString()).observe(this) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressFindCity.root.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressFindCity.root.visibility = View.GONE
                        it.data?.let { cities ->
                            adapter.setListCities(cities)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressFindCity.root.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            it.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }
}
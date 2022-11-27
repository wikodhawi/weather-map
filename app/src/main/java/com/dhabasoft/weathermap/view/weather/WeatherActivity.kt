package com.dhabasoft.weathermap.view.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.databinding.ActivityWeatherBinding
import com.dhabasoft.weathermap.view.stories.StoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                findCity()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.edtSearchCities.windowToken, 0)
            }
            false
        }
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
//                                        Toast.makeText(
//                                            applicationContext,
//                                            "${it.data?.message} ${getString(R.string.now_you_can_login)}",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
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
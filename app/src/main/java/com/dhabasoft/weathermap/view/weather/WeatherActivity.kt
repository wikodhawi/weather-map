package com.dhabasoft.weathermap.view.weather

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.databinding.ActivityWeatherBinding
import com.dhabasoft.weathermap.utils.customview.CustomDialog
import com.dhabasoft.weathermap.view.detailcity.DetailCityActivity
import com.dhabasoft.weathermap.view.weather.adapter.CityAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var adapter: CityAdapter
    private lateinit var dialog: Dialog

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

        binding.swipeRefresh.setOnRefreshListener {
            findCity()
            binding.swipeRefresh.isRefreshing = false
        }

        findCity()
    }

    private fun initRecyclerView() {
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rcyMain.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
        }
        adapter = CityAdapter(object : CityAdapter.OnClickCallback {
            override fun toDetailCity(cityFavouriteEntity: CityEntity) {
                val intent = Intent(applicationContext, DetailCityActivity::class.java)
                intent.putExtra(DetailCityActivity.KEY_SELECTED_CITY, cityFavouriteEntity)
                startActivity(intent)
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
                        val message = it.message!!
                        if (!this::dialog.isInitialized || !dialog.isShowing) {
                            dialog = CustomDialog.createDialogWithTwoButton(this,
                                cancelable = false,
                                message = message,
                                labelPositiveButton = getString(R.string.retry),
                                labelNegativeButton = getString(R.string.settings),
                                positiveAction = {
                                    findCity()
                                },
                                negativeAction = {
                                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                                },
                                closeAction = {

                                })
                            dialog.show()
                        }
                    }
                }
            }
        }
    }
}
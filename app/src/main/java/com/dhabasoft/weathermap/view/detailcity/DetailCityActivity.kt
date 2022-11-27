package com.dhabasoft.weathermap.view.detailcity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.databinding.ActivityDetailCityBinding
import com.dhabasoft.weathermap.utils.CountryFlags
import com.dhabasoft.weathermap.utils.customview.CustomDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCityBinding
    private var selectedCity: CityEntity? = null
    private val viewModel: DetailCityViewModel by viewModels()
    private lateinit var dialog: Dialog

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

        viewModel.getIsFavourite(selectedCity?.id ?: 0)

        getDetailCity()
    }

    private fun getDetailCity() {
        viewModel.getDetailCity(selectedCity?.id.toString()).observe(this) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressDetailCity.root.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressDetailCity.root.visibility = View.GONE
                        it.data?.let { cities ->
//                            adapter.setListCities(cities)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressDetailCity.root.visibility = View.GONE
                        val message = it.message!!
                        if (!this::dialog.isInitialized || !dialog.isShowing) {
                            dialog = CustomDialog.createDialogWithTwoButton(this,
                                cancelable = false,
                                message = message,
                                labelPositiveButton = getString(R.string.retry),
                                labelNegativeButton = getString(R.string.settings),
                                positiveAction = {
                                    getDetailCity()
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
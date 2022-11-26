package com.dhabasoft.androidintermediate.view.maps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dhabasoft.androidintermediate.R
import com.dhabasoft.androidintermediate.core.MyPreferences
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.dhabasoft.androidintermediate.databinding.ActivityMapsBinding
import com.dhabasoft.androidintermediate.view.detailstory.DetailStoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels()
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = MyPreferences(applicationContext).getToken()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val markerIdMapping = mutableMapOf<Marker?, Story>()
        val sydney = LatLng(-34.0, 151.0)
        viewModel.getStoriesLocation(token).observe(this) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressMaps.root.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressMaps.root.visibility = View.GONE
                        for (story in it.data ?: listOf()) {
                            val position =
                                LatLng(story.lat?.toDouble() ?: 0.0, story.lon?.toDouble() ?: 0.0)
                            val markerOption = MarkerOptions().position(position).title(story.name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))

                            val marker = mMap.addMarker(markerOption)
                            markerIdMapping[marker] = story
                        }
                    }
                    is Resource.Error -> {
                        binding.progressMaps.root.visibility = View.GONE
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        mMap.setOnInfoWindowClickListener {
            val selectedStory = markerIdMapping[it]
            val intent =
                Intent(applicationContext, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.KEY_DATA_STORY, selectedStory)
            startActivity(intent)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }
}
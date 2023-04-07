package com.sandoval.recipesapp.ui.recipes_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sandoval.recipesapp.R
import com.sandoval.recipesapp.databinding.FragmentMapsRecipesFragmentsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsRecipesFragments : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsRecipesFragmentsBinding
    private lateinit var map: GoogleMap

    private val recipe1 = LatLng(-34.0, 151.0)
    private val recipe2 = LatLng(-31.083332, 150.916672)
    private val recipe3 = LatLng(-32.916668, 151.750000)
    private val recipe4 = LatLng(-27.470125, 153.021072)

    private lateinit var latLngArrayList: ArrayList<LatLng?>
    lateinit var locationNameArraylist: ArrayList<String?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapsRecipesFragmentsBinding.inflate(
            inflater, container, false
        )

        latLngArrayList = ArrayList()
        locationNameArraylist = ArrayList()

        latLngArrayList.add(recipe1)
        locationNameArraylist.add("Cannelli Bean and Asparagus Salad")
        latLngArrayList.add(recipe2);
        locationNameArraylist.add("Slow Cooker Beef Stew")
        latLngArrayList.add(recipe3);
        locationNameArraylist.add("Easy Homemade Rice and Beans")
        latLngArrayList.add(recipe4);
        locationNameArraylist.add("Nigerian Snail Stew")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createMapFragment()

    }

    private fun createMapFragment() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker(map)
    }

    private fun createMarker(mMap: GoogleMap) {
        for (i in latLngArrayList.indices) {

            mMap.addMarker(
                MarkerOptions().position(latLngArrayList[i]!!)
                    .title("Marker in " + locationNameArraylist[i])
            )

            mMap.moveCamera(CameraUpdateFactory.newLatLng(recipe1))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(recipe1, 10f))
        }

        mMap.setOnMarkerClickListener { marker ->
            val markerName = marker.title
            Toast.makeText(
                activity, "Clicked location is $markerName", Toast.LENGTH_SHORT
            ).show()
            false
        }
    }
}
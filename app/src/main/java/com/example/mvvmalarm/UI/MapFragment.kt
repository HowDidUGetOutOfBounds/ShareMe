package com.example.mvvmalarm.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.mvvmalarm.R
import com.example.mvvmalarm.ThisApplication
import com.example.mvvmalarm.Utills.EventObserver
import com.example.mvvmalarm.Utills.Utils.POINT_FROM_MARKER
import com.example.mvvmalarm.Utills.Utils.POINT_FROM_MARKER_VALUE
import com.example.mvvmalarm.Utills.Utils.POINT_MESSAGE
import com.example.mvvmalarm.Utills.Utils.getDate
import com.example.mvvmalarm.Utills.Utils.shareMe
import com.example.mvvmalarm.data.CustomPoint
import com.example.mvvmalarm.data.PointDAO
import com.example.mvvmalarm.databinding.FragmentMapBinding
import com.example.mvvmalarm.model.MainViewModelFactory
import com.example.mvvmalarm.repository.MainActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import javax.inject.Inject


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    @Inject
    lateinit var dataSource: PointDAO

    private var _binding: FragmentMapBinding? = null
    private lateinit var binding: FragmentMapBinding

    private val mapFragmentViewModel: MainActivityViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        _binding = binding
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpMap()

        mapFragmentViewModel.isGeoInProgress.observe(requireActivity()) { inProgress ->
            binding.shareMeButton.isEnabled = !inProgress
            binding.pointButton.isEnabled = !inProgress


            if (mapFragmentViewModel.locationUpdatedEvent && !inProgress) {
                val cameraUpdate = mapFragmentViewModel.getCameraUpdate()
                mMap.animateCamera(cameraUpdate)
                mapFragmentViewModel.locationUpdatedEvent = false
            }
        }

        binding.shareMeButton.setOnClickListener {
            val sendIntent = shareMe(mapFragmentViewModel.getGeoData())

            val shareIntent = Intent.createChooser(sendIntent, "Share via")
            startActivity(shareIntent)
        }

        binding.pointButton.setOnClickListener {
            val point = CustomPoint(
                lat = mapFragmentViewModel.latitude.value!!,
                lon = mapFragmentViewModel.longitude.value!!,
                date = getDate(),
                message = getDate()
            )
            dataSource.insertPoint(point)

            findNavController().navigate(R.id.fragmentCustomization)
        }

        binding.deleteButton.setOnClickListener {
            dataSource.deleteAll()
            mMap.clear()
        }
    }

    private fun setUpMap() {

        val mapFragment =
            childFragmentManager.findFragmentById(binding.mapFragment.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        mMap.clear()

        lifecycle.coroutineScope.launch {
            mapFragmentViewModel.getAllPointsFromDB().collect() { pointList ->
                for (point in pointList) {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(point.lat, point.lon))
                            .title(point.message)
                    )
                }
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val bundle = Bundle()
        bundle.putInt(POINT_FROM_MARKER, POINT_FROM_MARKER_VALUE)
        bundle.putString(POINT_MESSAGE, marker.title)

        findNavController().navigate(R.id.fragmentCustomization, bundle)
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as ThisApplication).appComponent.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



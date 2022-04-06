package com.example.mvvmalarm.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mvvmalarm.R
import com.example.mvvmalarm.ThisApplication
import com.example.mvvmalarm.Utills.Utils.GALLERY_IMAGE_REQ_CODE
import com.example.mvvmalarm.Utills.Utils.POINT_FROM_LOCATION_VALUE
import com.example.mvvmalarm.Utills.Utils.POINT_FROM_MARKER
import com.example.mvvmalarm.Utills.Utils.POINT_FROM_MARKER_VALUE
import com.example.mvvmalarm.Utills.Utils.POINT_MESSAGE
import com.example.mvvmalarm.Utills.Utils.shareMe
import com.example.mvvmalarm.data.CustomPoint
import com.example.mvvmalarm.data.PointDAO
import com.example.mvvmalarm.data.PointDatabase
import com.example.mvvmalarm.databinding.FragmentCustomizationBinding
import com.example.mvvmalarm.databinding.FragmentMapBinding
import com.example.mvvmalarm.model.MainViewModelFactory
import com.example.mvvmalarm.repository.MainActivityViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.maps.GoogleMap
import javax.inject.Inject


class FragmentCustomization : Fragment() {


    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    @Inject
    lateinit var dataSource: PointDAO

    private var _binding: FragmentCustomizationBinding? = null
    private lateinit var binding: FragmentCustomizationBinding

    private val fragmentCustomizationViewModel: MainActivityViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomizationBinding.inflate(inflater, container, false)
        _binding = binding
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentCustomizationViewModel.imageUri.observe(requireActivity()) { uri ->
            binding.chooseImage.setImageURI(uri)
        }


        var loadedPoint: CustomPoint =
            if ((arguments != null) && (arguments?.getInt(
                    POINT_FROM_MARKER,
                    POINT_FROM_LOCATION_VALUE
                ) != POINT_FROM_LOCATION_VALUE
                        )
            ) {
                fragmentCustomizationViewModel.getPointByMessage(
                    requireArguments().getString(
                        POINT_MESSAGE,
                        ""
                    )
                )
            } else {
                fragmentCustomizationViewModel.getLastPoint()
            }

        with(binding)
        {
            date.text = loadedPoint.date
            lat.text = String.format("latitude: %.4f", loadedPoint.lat)
            lon.text = String.format("longitude: %.4f", loadedPoint.lon)
            messageEditText.setText(loadedPoint.message)
            if(loadedPoint.imgUri != null) {
                binding.chooseImage.setImageURI(Uri.parse(loadedPoint.imgUri))
            }

            buttonSave.setOnClickListener {
                val updatedPoint = loadedPoint
                updatedPoint.message = messageEditText.text.toString()
                if(fragmentCustomizationViewModel.imageUri.value != null) {
                    updatedPoint.imgUri = fragmentCustomizationViewModel.imageUri.value.toString()
                }

                dataSource.update(updatedPoint)
                findNavController().popBackStack()
            }

            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonDelete.setOnClickListener {
                dataSource.deletePoint(loadedPoint)
                findNavController().popBackStack()
            }

            buttonShareMe.setOnClickListener {
                val sendIntent = shareMe(fragmentCustomizationViewModel.getGeoData(loadedPoint))

                val shareIntent = Intent.createChooser(sendIntent, "Share this via")
                startActivity(shareIntent)
            }

            chooseImage.setOnClickListener {
                Log.d("TAG", "onViewCreated: ")
                ImagePicker.with(this@FragmentCustomization)
                    .cropSquare()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent {
                        intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                fragmentCustomizationViewModel.imageUri.postValue(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Log.d("TAG", "onActivityResult:  ${ImagePicker.getError(data)}")
            }
        }




    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as ThisApplication).appComponent.inject(this)
        //Inject here

    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentCustomizationViewModel.imageUri.value = null
        _binding = null
    }

}
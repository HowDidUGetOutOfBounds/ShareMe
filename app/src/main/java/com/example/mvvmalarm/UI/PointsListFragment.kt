package com.example.mvvmalarm.UI

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmalarm.R
import com.example.mvvmalarm.ThisApplication
import com.example.mvvmalarm.Utills.Utils
import com.example.mvvmalarm.data.PointDAO
import com.example.mvvmalarm.databinding.FragmentMapBinding
import com.example.mvvmalarm.databinding.FragmentPointsListBinding
import com.example.mvvmalarm.model.MainViewModelFactory
import com.example.mvvmalarm.repository.MainActivityViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory


    private var _binding: FragmentPointsListBinding? = null
    private lateinit var binding: FragmentPointsListBinding

    private val pointListFragmentViewModel: MainActivityViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var recyclerView: RecyclerView


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as ThisApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPointsListBinding.inflate(inflater, container, false)
        _binding = binding
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val pointsAdapter = PointAdapter { point ->
            val bundle = Bundle()
            bundle.putInt(Utils.POINT_FROM_MARKER, Utils.POINT_FROM_MARKER_VALUE)
            bundle.putString(Utils.POINT_MESSAGE, point.message)

            findNavController().navigate(R.id.fragmentCustomization, bundle)
        }

        recyclerView.adapter = pointsAdapter


        lifecycle.coroutineScope.launch{
            pointListFragmentViewModel.getAllPointsFromDB().collect() {
                pointsAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
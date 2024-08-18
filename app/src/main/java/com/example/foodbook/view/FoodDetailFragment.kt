package com.example.foodbook.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodbook.databinding.FragmentFoodDetailBinding
import com.example.foodbook.databinding.FragmentFoodListBinding
import com.example.foodbook.util.downloadPics
import com.example.foodbook.util.makePlaceHolder
import com.example.foodbook.viewmodel.FoodDetailViewModel


class FoodDetailFragment : Fragment() {
    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FoodDetailViewModel
    var foodId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodDetailViewModel::class.java]
        arguments?.let {
            foodId = FoodDetailFragmentArgs.fromBundle(it).foodId
        }

        viewModel.roomData(foodId)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.foodLiveData.observe(viewLifecycleOwner) {
            binding.foodName.text = it.name
            binding.foodCalorie.text = it.calorie
            binding.foodProtein.text = it.protein
            binding.foodCarbs.text = it.carbs
            binding.foodFat.text = it.fat
            binding.foodImage.downloadPics(it.picture, makePlaceHolder(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
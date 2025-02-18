package com.example.foodbook.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodbook.adapter.FoodRecyclerAdapter
import com.example.foodbook.databinding.FragmentFoodListBinding
import com.example.foodbook.service.FoodAPI
import com.example.foodbook.viewmodel.FoodListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FoodListFragment : Fragment() {


    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FoodListViewModel
    private val foodRecyclerAdapter = FoodRecyclerAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodListViewModel::class.java]
        viewModel.refreshData()

        binding.foodRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.foodRecyclerView.adapter = foodRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.foodRecyclerView.visibility = View.GONE
            binding.errorMessage.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.refreshDataFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false

        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.foods.observe(viewLifecycleOwner) {
            binding.foodRecyclerView.visibility = View.VISIBLE
            foodRecyclerAdapter.updateFoodList(it)
        }

        viewModel.foodErrorMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.foodRecyclerView.visibility = View.GONE
            } else {
                binding.errorMessage.visibility = View.GONE
            }
        }

        viewModel.uploadingFood.observe(viewLifecycleOwner) {
            if (it) {
                binding.errorMessage.visibility = View.GONE
                binding.foodRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
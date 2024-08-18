package com.example.foodbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbook.databinding.FoodRecyclerRowBinding
import com.example.foodbook.databinding.FragmentFoodDetailBinding
import com.example.foodbook.model.Food
import com.example.foodbook.util.downloadPics
import com.example.foodbook.util.makePlaceHolder
import com.example.foodbook.view.FoodListFragmentDirections

class FoodRecyclerAdapter(val foodList: ArrayList<Food>) :
    RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder>() {

    class FoodViewHolder(val binding: FoodRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding =
            FoodRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun updateFoodList(newFoodList: List<Food>) {
        foodList.clear()
        foodList.addAll(newFoodList)
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.binding.name.text = foodList[position].name
        holder.binding.calorie.text = foodList[position].calorie

        holder.itemView.setOnClickListener {
            val action =
                FoodListFragmentDirections.actionFoodListFragmentToFoodDetailFragment(foodList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.downloadPics(
            foodList.get(position).picture,
            makePlaceHolder(holder.itemView.context)
        )
    }
}
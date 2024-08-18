package com.example.foodbook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodbook.model.Food
import com.example.foodbook.roomdb.FoodDataBase
import kotlinx.coroutines.launch

class FoodDetailViewModel(application: Application) : AndroidViewModel(application) {

    val foodLiveData = MutableLiveData<Food>()

    fun roomData(uuid: Int) {
        viewModelScope.launch {
            val dao = FoodDataBase(getApplication()).foodDao()
            val food = dao.getFood(uuid)
            foodLiveData.value = food
        }
    }
}
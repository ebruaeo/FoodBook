package com.example.foodbook.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodbook.model.Food
import com.example.foodbook.roomdb.FoodDataBase
import com.example.foodbook.service.FoodAPIService
import com.example.foodbook.util.PrivateSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodListViewModel(application: Application) : AndroidViewModel(application) {
    val foods = MutableLiveData<List<Food>>()
    val foodErrorMessage = MutableLiveData<Boolean>()
    val uploadingFood = MutableLiveData<Boolean>()
    private val updatingTime = 10 * 60 * 1000 * 1000 * 1000L


    private val foodAPIService = FoodAPIService()
    private val privateSharedPreferences = PrivateSharedPreferences(getApplication())

     fun refreshData() {
        val savingTime = privateSharedPreferences.getTime()

        if (savingTime != null && savingTime != 0L && System.nanoTime() - savingTime < updatingTime) {
            takeDatasFromRoom()
        } else {
            takeDataFromInternet()
        }
    }

    fun refreshDataFromInternet (){
        takeDataFromInternet()
    }

    private fun takeDatasFromRoom() {
        uploadingFood.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val foodList = FoodDataBase(getApplication()).foodDao().getAllFood()
            withContext(Dispatchers.Main) {
                showFoods(foodList)
                Toast.makeText(getApplication(), "We took foods from room", Toast.LENGTH_LONG)
                    .show()

            }
        }
    }

    private fun takeDataFromInternet() {
        uploadingFood.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val foodList = foodAPIService.getData()
            withContext(Dispatchers.Main) {
                uploadingFood.value = false
                saveToRoom(foodList)
                Toast.makeText(getApplication(), "We took foods from internet", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun showFoods(foodList: List<Food>) {
        foods.value = foodList
        foodErrorMessage.value = false
        uploadingFood.value = false
    }

    private fun saveToRoom(foodList: List<Food>) {
        viewModelScope.launch {
            val dao = FoodDataBase(getApplication()).foodDao()
            dao.deleteAll()
            val uuidList = dao.insertAll(*foodList.toTypedArray())
            var i = 0
            while (i < foodList.size) {
                foodList[i].uuid = uuidList[i].toInt()
                i += 1
            }

            showFoods(foodList)
        }

        privateSharedPreferences.saveTime(System.nanoTime())
    }


}
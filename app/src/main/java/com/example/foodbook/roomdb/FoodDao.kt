package com.example.foodbook.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodbook.model.Food

@Dao
interface FoodDao {

    @Insert
    suspend fun insertAll(vararg food: Food): List<Long>
    // eklediği besinlerin idsini long olarak geri veriyor.

    @Query("SELECT * FROM food")
    suspend fun getAllFood(): List<Food>

    @Query("SELECT * FROM food WHERE uuid = :foodId")
    suspend fun getFood(foodId: Int): Food

    @Query("DELETE FROM food")
    suspend fun deleteAll()


}
package com.example.foodbook.model

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("isim")
    val name: String,
    @SerializedName("kalori")
    val calorie: String,
    @SerializedName("karbonhidrat")
    val carbs: String,
    @SerializedName("protein")
    val protein: String,
    @SerializedName("yag")
    val fat: String,
    @SerializedName("gorsel")
    val picture: String
)

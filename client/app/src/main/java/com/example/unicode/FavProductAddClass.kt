package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavProductAddClass(
    @Expose
    @SerializedName("pv_id") val pv_id: Int,

    @Expose
    @SerializedName("product_id") val product_id: Int,

    @Expose
    @SerializedName("favorite_id") val favorite_id: Int,
)

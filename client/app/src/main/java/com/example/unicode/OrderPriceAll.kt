package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderPriceAll(
    @Expose
    @SerializedName("price_all") val price_all:Int
)

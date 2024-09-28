package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderDetailShopBagProduct(
    @Expose
    @SerializedName("product_name") val product_name: String,
    @Expose
    @SerializedName("amount") val amount: Int,
    @Expose
    @SerializedName("price") val price: Int,
    @Expose
    @SerializedName("priceAll") val priceAll: Int,
)

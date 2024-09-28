package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @Expose
    @SerializedName("user_id") val user_id: Int,

    @Expose
    @SerializedName("amount") val amount: Int,

    @Expose
    @SerializedName("product_price") val product_price: Int,

    @Expose
    @SerializedName("price_all") val price_all: Int,

    @Expose
    @SerializedName("order_id") val order_id: Int,

    @Expose
    @SerializedName("product_id") val product_id: Int,

    @Expose
    @SerializedName("size_id") val size_id: Int,


)

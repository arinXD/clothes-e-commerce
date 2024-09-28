package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderProductClass(
    @Expose
    @SerializedName("order_id") val order_id: Int,
    @Expose
    @SerializedName("pay_status") val pay_status: String,
    @Expose
    @SerializedName("transport_fee") val transport_fee: Int,
    @Expose
    @SerializedName("user_address_id") val user_address_id: Int,
    @Expose
    @SerializedName("created_at") val created_at: String,
    @Expose
    @SerializedName("order_datial_id") val order_datial_id: Int,
    @Expose
    @SerializedName("amount") val amount: Int,
    @Expose
    @SerializedName("product_price") val product_price: Int,
    @Expose
    @SerializedName("price_all") val price_all: Int,
    @Expose
    @SerializedName("product_id") val product_id: Int,
    @Expose
    @SerializedName("product_name") val product_name: String,
    @Expose
    @SerializedName("detail") val detail: String,
    @Expose
    @SerializedName("photo") val photo: String,
    @Expose
    @SerializedName("size") val size: String,
)

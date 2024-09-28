package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductClass(
    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("product_name") val product_name: String,

    @Expose
    @SerializedName("price") val price: Int,

    @Expose
    @SerializedName("detail") val detail: String,

    @Expose
    @SerializedName("photo") val photo: String,

    @Expose
    @SerializedName("amount") val amount: Int,

    @Expose
    @SerializedName("subtype_id") val subtype_id: Int,
)

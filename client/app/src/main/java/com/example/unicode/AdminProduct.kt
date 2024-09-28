package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminProduct(
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
    @SerializedName("product_type") val product_type: Int){}
//
//    @Expose
//    @SerializedName("subtype_name") val subtype_name: String,
//
//    @Expose
//    @SerializedName("product_type_id") val product_type_id: Int,
//
//    @Expose
//    @SerializedName("type_name") val type_name: String){}


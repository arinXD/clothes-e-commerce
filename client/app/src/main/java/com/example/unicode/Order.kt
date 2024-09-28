package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Order (

    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("pay_status") val pay_status: String,

    @Expose
    @SerializedName("transport_fee") val transport_fee: Int,

    @Expose
    @SerializedName("user_address_id") val user_address_id: Int,

    @Expose
    @SerializedName("credit_card_id") val credit_card_id: Int,

    @Expose
    @SerializedName("user_id") val user_id: Int,

    @Expose
    @SerializedName("created_at") val created_at: String,

    @Expose
    @SerializedName("updated_at") val updated_at: String)

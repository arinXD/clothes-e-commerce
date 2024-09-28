package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class transport_status(
    @Expose
    @SerializedName("id") val id: Int,
    @Expose
    @SerializedName("title") val title: String, )

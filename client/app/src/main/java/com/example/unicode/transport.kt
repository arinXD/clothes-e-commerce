package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class transport(
    @Expose
    @SerializedName("id") val id: Int,
    @Expose
    @SerializedName("detail") val detail: String, )

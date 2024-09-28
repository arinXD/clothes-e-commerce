package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SizeClass(
    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("size") val size: String
)

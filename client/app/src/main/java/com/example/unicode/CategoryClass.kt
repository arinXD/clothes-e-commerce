package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryClass(
    @Expose
    @SerializedName("id") val id: Int,
    @Expose
    @SerializedName("type_name") val type_name: String,
    @Expose
    @SerializedName("type_photo") val type_photo: String,
)

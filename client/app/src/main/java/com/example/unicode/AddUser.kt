package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddUser(
    @Expose
    @SerializedName("status") val status: Int,
)

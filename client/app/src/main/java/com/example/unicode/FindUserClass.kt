package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FindUserClass(
    @Expose
    @SerializedName("email") val email:String,
    @Expose
    @SerializedName("user_name") val user_name:String,
)

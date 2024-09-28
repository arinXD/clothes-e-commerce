package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class producttypeClass (
    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("type_name") val type_name: String
)
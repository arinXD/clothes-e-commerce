package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransportDetailClass(
    @Expose
    @SerializedName("title") val title: String,

    @Expose
    @SerializedName("detail") val detail: String,

    @Expose
    @SerializedName("created_at") val created_at: String,

    @Expose
    @SerializedName("photo") val photo: String,
)

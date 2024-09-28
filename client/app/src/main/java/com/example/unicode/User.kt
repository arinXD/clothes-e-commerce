package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
//"status": 1,
//"user_id": 2,
//"user_type": "user",
//"user_name": "oat",
//"email": "oat@gmail.com",
//"gender": "1234",
//"birthday": "2022-08-31T17:00:00.000Z"
    @Expose
    @SerializedName("status") val status: Int,

    @Expose
    @SerializedName("user_id") val user_id: Int,

    @Expose
    @SerializedName("user_type") val user_type: String,

    @Expose
    @SerializedName("user_name") val user_name: String,

    @Expose
    @SerializedName("email")val email: String,

//    @Expose
//    @SerializedName("user_password") val user_password: String,

    @Expose
    @SerializedName("gender") val gender: String,

    @Expose
    @SerializedName("birthday") val birthday: String
)

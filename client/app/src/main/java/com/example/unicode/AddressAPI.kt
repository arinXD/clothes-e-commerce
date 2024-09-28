package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AddressAPI {
    @GET("allAddress/{id}")
    fun retrieveAddress(
        @Path("id") id: Int
    ): Call<List<Address>>

    @FormUrlEncoded
    @POST("address")
    fun insertAddr(
//        @Field("id") id: String,
        @Field("address") address: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("zip_code") zip_code: String,
        @Field("phone") phone: Int,
        @Field("user_id") user_id: Int): Call<Address>

    @FormUrlEncoded
    @PUT("address/{id}")
    fun updateAddress(
        @Path("id") id: Int,
        @Field("address") address: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("zip_code") zip_code: String,
        @Field("phone") phone: Int,
        @Field("user_id") user_id: Int): Call<Address>

    @DELETE("/address/{id}") /// Delete
    fun deleteAddress(
        @Path("id") id: Int): Call<Address>

    companion object {
        fun create(): AddressAPI {
            val addressClient : AddressAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AddressAPI ::class.java)
            return addressClient
        }
    }


}
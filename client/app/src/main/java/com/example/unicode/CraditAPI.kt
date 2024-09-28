package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CraditAPI {
    @GET("credit/{user_id}")
    fun myCredit(
        @Path("user_id") user_id: Int
    ): Call<List<Credit>>

    @FormUrlEncoded
    @POST("cradit")
    fun insertCradit(
//        @Field("id") id: String,
        @Field("firstname") firstname: String,
        @Field("card_no") card_no: String,
        @Field("expire_date") expire_date: String,
        @Field("CVV") cvv: Int,
        @Field("user_id") user_id: Int): Call<Credit>

    @FormUrlEncoded
    @PUT("cradit/{id}")
    fun updateCradit(
        @Path("id") id: Int,
        @Field("firstname") firstname: String,
        @Field("card_no") card_no: String,
        @Field("expire_date") expire_date: String,
        @Field("CVV") cvv: Int,
        @Field("user_id") user_id: Int): Call<Credit>

    @DELETE("/cradit/{id}") /// Delete
    fun deleteCradit(
        @Path("id") id: Int): Call<Credit>

    companion object {
        fun create(): CraditAPI {
            val craditClient : CraditAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CraditAPI ::class.java)
            return craditClient
        }
    }
}
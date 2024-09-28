package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface FavAPI {

    @GET("fav/add/{user_id}/{product_id}")
    fun addFav(
        @Path("user_id") user_id: Int,
        @Path("product_id") product_id: Int,
    ): Call<FavProductAddClass>

    @GET("fav/product/{user_id}")
    fun favProduct(
        @Path("user_id") user_id: Int
    ): Call<List<FavProduct>>

    @DELETE("/fav/product/{pv_id}")
    fun deleteFav(
        @Path("pv_id") pv_id: Int
    ): Call<FavProduct>

    companion object {
        fun create(): FavAPI {
            val favClient : FavAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FavAPI ::class.java)
            return favClient
        }
    }
}
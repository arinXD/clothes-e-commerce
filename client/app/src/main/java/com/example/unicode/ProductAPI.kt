package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductAPI {

    @GET("/find/detail/{product_id}")
    fun findProductDetail(
        @Path("product_id") type_id: Int
    ): Call<ProductClass>

    @GET("/find/amount/{product_id}")
    fun findProductAmount(
        @Path("product_id") type_id: Int
    ): Call<ProductClass>

    @GET("type/product/{type_id}")
    fun productTypeAll(
        @Path("type_id") type_id: Int
    ): Call<List<ProductClass>>

    @GET("products/all")
    fun productAll(): Call<List<ProductClass>>

    @GET("products/last")
    fun productLast(): Call<List<ProductClass>>

    @GET("products/size/{id}")
    fun findProductSize(
        @Path("id") id: Int
    ): Call<List<SizeClass>>

    @GET("product/type")
    fun getAllProductType(): Call<List<CategoryClass>>

    companion object {
        fun create(): ProductAPI {
            val productClient : ProductAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductAPI ::class.java)
            return productClient
        }
    }

}
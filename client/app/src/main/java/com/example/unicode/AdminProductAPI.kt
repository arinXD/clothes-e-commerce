package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AdminProductAPI {
    @GET("allproduct")
    fun retrieveProduct(): Call<List<AdminProduct>>

    @GET("product/type")
    fun getAllType(): Call<List<ProductType>>

    @FormUrlEncoded
    @POST("add/type")
    fun addType(
        @Field(" ") type_name:String
    ): Call<ProductType>


    @FormUrlEncoded
    @POST("product")
    fun insertProduct(
        @Field("product_name") product_name: String,
        @Field("price") price: Int,
        @Field("detail") detail: String,
        @Field("photo") photo: String,
        @Field("amount") amount: Int,
        @Field("product_type") product_type: Int,
//        @Field("size_list") size_list: ArrayList<Int>
    ): Call<AdminProduct>


    @FormUrlEncoded
    @PUT("product/update")
    fun updateProduct(
        @Field("id") id: Int,
        @Field("product_name") product_name: String,
        @Field("price") price: Int,
        @Field("detail") detail: String,
        @Field("photo") photo: String,
        @Field("amount") amount: Int,
        @Field("product_type") product_type: Int,
//        @Field("add_size") add_size: ArrayList<Int>,
//        @Field("delete_size") delete_size: ArrayList<Int>,
    ): Call<AdminProduct>

    @FormUrlEncoded
    @POST("add/product/size")
    fun addProductSize(
        @Field("product_id") product_id: Int,
        @Field("size_id") size_id: Int,
    ): Call<SizeClass>

    @FormUrlEncoded
    @POST("delete/product/size")
    fun deleteProductSize(
        @Field("product_id") product_id: Int,
        @Field("size_id") size_id: Int,
    ): Call<SizeClass>


    @DELETE("product/{id}")
    fun daleteProduct(
        @Path("id") id: Int
    ): Call<AdminProduct>

    @GET("allproduct_type")
    fun retrieveProductType(): Call<List<producttypeClass>>

    //    /product/size/:product_id
    @GET("/product/size/{product_id}")
    fun findProductSize(
        @Path("product_id") product_id: Int
    ): Call<List<SizeClass>>

    companion object {
        fun create(): AdminProductAPI {
            val client: AdminProductAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AdminProductAPI::class.java)
            return client
        }
    }

}
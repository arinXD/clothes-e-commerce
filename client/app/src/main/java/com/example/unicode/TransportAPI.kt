package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TransportAPI {

    @GET("/add/success/receive/order/{order_id}")
    fun updateReceiveSuccess(
        @Path("order_id") order_id: Int
    ): Call<OrderForTransport>

    @FormUrlEncoded
    @POST("/add/detail/transport")
    fun insertTransport(
        @Field("detail") detail: String,
        @Field("transport_status_id") transport_status_id:Int,
        @Field("order_id") order_id:Int
    ): Call<TransportDetailClass>

    @GET("/receive/order/transport/{receive_status}")
    fun orderForTransport(
        @Path("receive_status") receive_status:String
    ): Call<List<OrderForTransport>>

    @GET("alltransport")
    fun retrieveTransport(): Call<List<transport>>

//    @FormUrlEncoded
//    @POST("transport")
//    fun insertTransport(
////        @Field("id") id: String,
//        @Field("detail") detail: String,
//        @Field("transport_status_id") transport_status_id: Int,
//        @Field("order_id") order_id: Int ): Call<transport>

    @GET("alltransport_status")
    fun retrieveTransporttt(): Call<List<transport_status>>

    companion object {
        fun create(): TransportAPI {
            val Client: TransportAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TransportAPI::class.java)
            return Client
        }
    }
}
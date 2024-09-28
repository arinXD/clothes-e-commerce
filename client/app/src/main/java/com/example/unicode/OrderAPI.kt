package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface OrderAPI {
    @GET("transport/detail/{order_id}")
    fun getTransportDetail(
        @Path("order_id") order_id:Int
    ):Call <List<TransportDetailClass>>

    @GET("order/history/product/{order_id}")
    fun orderHistoryProduct(
        @Path("order_id") order_id: Int
    ): Call<List<OrderDetailShopBagProduct>>

    @GET("/order/{order_id}")
    fun findOrder(
        @Path("order_id") order_id: Int
    ): Call<Order>

    //    /order/product/:user_id
    @GET("order/product/{user_id}")
    fun callProduct(
        @Path("user_id") user_id: Int
    ): Call<List<OrderProductClass>>

    @GET("order/history/{user_id}")
    fun orderHistory(
        @Path("user_id") user_id: Int
    ): Call<List<Order>>

    //    /order/:address_id
    @GET("order/address/{address_id}")
    fun orderAddress(
        @Path("address_id") id: Int
    ): Call<AddressClass>

    //  /order/detail/:order_id
    @GET("order/detail/{order_id}")
    fun findOrderDetail(
        @Path("order_id") order_id: Int
    ): Call<List<OrderDetail>>

    @GET("order/credit/{credit_card_id}")
    fun orderCredit(
        @Path("credit_card_id") id: Int
    ): Call<Credit>

    @GET("myOrder/{id}")
    fun retrieveOrder(
        @Path("id") id: Int
    ): Call<Order>

    @FormUrlEncoded
    @POST("addOrder")
    fun addOrder(
        @Field("user_id") user_id: Int,
        @Field("amount") amount: Int,
        @Field("product_price") product_price: Int,
        @Field("price_all") price_all: Int,
        @Field("order_id") order_id: Int,
        @Field("product_id") product_id: Int,
        @Field("size_id") size_id: Int,
    ): Call<OrderDetail>

    @DELETE("delete/order/{order_datial_id}")
    fun deleteOrderDetail(
        @Path("order_datial_id") id: Int
    ): Call<OrderProductClass>

    @PUT("order/address/{order_id}/{address_id}")
    fun updateOrderAddress(
        @Path("order_id") order_id: Int,
        @Path("address_id") address_id: Int,
    ): Call<Order>

    @PUT("order/credit/{order_id}/{credit_card_id}")
    fun updateOrderCredit(
        @Path("order_id") order_id: Int,
        @Path("credit_card_id") credit_card_id: Int,
    ): Call<Order>

    @PUT("order/complete/{order_id}")
    fun orderComplete(
        @Path("order_id") order_id: Int
    ): Call<Order>
    
    @GET("find/price/all/{order_id}")
    fun findPriceAll(
        @Path("order_id") order_id:Int
    ): Call<OrderPriceAll>

    companion object {
        fun create(): OrderAPI {
            val Client: OrderAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrderAPI::class.java)
            return Client
        }
    }
}
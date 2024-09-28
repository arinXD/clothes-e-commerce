package com.example.unicode

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.OrderHistoryItemsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SelectOrderHistoryAdapter(private val orderHistoryList: ArrayList<Order>, val context: Context) :
    RecyclerView.Adapter<SelectOrderHistoryAdapter.ViewHolder>() {
        private val orderApi = OrderAPI.create()
    inner class ViewHolder(view: View, val binding: OrderHistoryItemsBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            var priceAll = 0
            binding.btnOrderDetail.setOnClickListener {
                var item = orderHistoryList[adapterPosition]
                orderApi.findOrderDetail(item.id).enqueue(object : Callback<List<OrderDetail>> {
                    override fun onResponse(call: Call<List<OrderDetail>>, response: Response<List<OrderDetail>>) {
                        if (response.isSuccessful) {
                            response.body()?.forEach {
                                priceAll+= it.price_all
                            }
                            binding.priceTxt.text = "รวมทั้งหมด:\n${priceAll} บาท"
                        } else {
                            Toast.makeText(context, "cant find order id", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<OrderDetail>>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(context, "error on failed" + t.message, Toast.LENGTH_LONG).show()
                    }
                })
                val intent = Intent(context, OrderHistoryDetail::class.java)
                intent.putExtra("orderId", item.id)
                view.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderHistoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = orderHistoryList!![position]
        var priceAll = 0

        binding.dateTxt.text = "สั่งซื้อวันที่:\n${formatDate(item.created_at)}"
        orderApi.findOrderDetail(item.id).enqueue(object : Callback<List<OrderDetail>> {
            override fun onResponse(call: Call<List<OrderDetail>>, response: Response<List<OrderDetail>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        priceAll+= it.price_all
                    }
                    binding.priceTxt.text = "รวมทั้งหมด:\n${priceAll} บาท"
                } else {
                    Toast.makeText(context, "cant find order id", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OrderDetail>>, t: Throwable) {
                println(t.message)
                Toast.makeText(context, "error on failed" + t.message, Toast.LENGTH_LONG).show()
            }
        })


    }

    override fun getItemCount(): Int {
        return orderHistoryList?.size ?: 0
    }
    private fun formatDate(dateString: String): String {
        // Parse the date string and format it to "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
}
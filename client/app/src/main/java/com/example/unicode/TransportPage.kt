package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityTransportPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TransportPage : AppCompatActivity() {
    private lateinit var binding: ActivityTransportPageBinding
    var orderList = arrayListOf<OrderForTransport>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTransportPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerViewTransport.adapter = OrderForTransportAdapter(orderList, applicationContext)
        binding.recyclerViewTransport.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewTransport.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerViewTransport.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )



    }
    override fun onResume() {
        super.onResume()
        var receiveStatus = intent.getStringExtra("receiveStatus").toString()
        orderForTransport(receiveStatus)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun orderForTransport(status: String){
        orderList.clear()
        val api = TransportAPI.create()
        api.orderForTransport(status)
            .enqueue(object : Callback<List<OrderForTransport>> {
                override fun onResponse(call: Call<List<OrderForTransport>>, response: Response<List<OrderForTransport>>) {
                    response.body()?.forEach{
                        orderList.add(OrderForTransport(it.id,it.pay_status, it.receive_status, it.transport_fee, it.user_address_id, it.credit_card_id, it.user_id, it.created_at, it.updated_at))
                    }
                    binding.recyclerViewTransport.adapter = OrderForTransportAdapter(orderList,applicationContext)
                }

                override fun onFailure(call: Call<List<OrderForTransport>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error on orderForTransport" + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }

}
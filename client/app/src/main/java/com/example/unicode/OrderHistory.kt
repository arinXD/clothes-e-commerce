package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityOrderHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistory : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryBinding
    var orderList = arrayListOf<Order>()
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        session = SessionManager(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rcv.adapter = SelectOrderHistoryAdapter(orderList,applicationContext)
        binding.rcv.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        binding.rcv.addItemDecoration(itemDecor)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        callOrderHistory(uId.toString().toInt())
    }
    fun callOrderHistory(uId: Int){
        orderList.clear()
        val orderAPI = OrderAPI.create()
        orderAPI.orderHistory(uId).enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response:Response<List<Order>>) {
//                println(response.body())
                response.body()?.reversed()?.forEach {
                    orderList.add(
                         Order(
                             it.id,
                             it.pay_status,
                             it.transport_fee,
                             it.user_address_id,
                             it.credit_card_id,
                             it.user_id,
                             it.created_at,
                             it.updated_at)
                    )
                }
                binding.rcv.adapter = SelectOrderHistoryAdapter(orderList, applicationContext)
            }
            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
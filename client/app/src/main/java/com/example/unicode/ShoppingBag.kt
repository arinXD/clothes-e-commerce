package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityShoppingBagBinding
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingBag : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingBagBinding
    public var productsList = arrayListOf<OrderProductClass>()
    public var priceList = arrayListOf<OrderDetailShopBagProduct>()
    var priceAll = 0
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShoppingBagBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        session = SessionManager(applicationContext)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rcvProduct.adapter = ShoppingBagAdapter(productsList,applicationContext)
        binding.rcvProduct.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        binding.rcvProduct.addItemDecoration(itemDecor)

//        binding.rcvPrice.adapter = OrderDetailShoppingBagAdapter(priceList,applicationContext)
//        binding.rcvPrice.layoutManager = LinearLayoutManager(applicationContext)
//        binding.rcvPrice.addItemDecoration(itemDecor)

        binding.btnBuy.setOnClickListener {
            var intent = Intent(applicationContext, CompleteProductPage::class.java)
            intent.putExtra("priceAll", priceAll)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        callProduct(uId.toString().toInt())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun callProduct(id: Int) {
        productsList.clear()
        priceList.clear()
        priceAll = 0
        val orderApi = OrderAPI.create()
        orderApi.callProduct(id)
            .enqueue(object : Callback<List<OrderProductClass>> {
                override fun onResponse(
                    call: Call<List<OrderProductClass>>, response:
                    Response<List<OrderProductClass>>
                ) {
                    println(response.body())
                    response.body()?.forEach {
                        priceAll+= it.price_all
                        productsList.add(
                            OrderProductClass(
                                it.order_id,
                                it.pay_status,
                                it.transport_fee,
                                it.user_address_id,
                                it.created_at,
                                it.order_datial_id,
                                it.amount,
                                it.product_price,
                                it.price_all,
                                it.product_id,
                                it.product_name,
                                it.detail,
                                it.photo,
                                it.size
                            )
                        )
                        priceList.add(OrderDetailShopBagProduct(
                            it.product_name,
                            it.amount,
                            it.product_price,
                            it.price_all
                        ))

                    }
                    binding.rcvProduct.adapter = ShoppingBagAdapter(productsList, applicationContext)
//                    binding.rcvPrice.adapter = OrderDetailShoppingBagAdapter(priceList, applicationContext)
                    binding.priceAll.text = priceAll.toString() +" บาท"
                }

                override fun onFailure(call: Call<List<OrderProductClass>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
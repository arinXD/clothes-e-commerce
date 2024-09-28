package com.example.unicode

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityInsertTransportBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InsertTransport : AppCompatActivity() {
    private lateinit var binding: ActivityInsertTransportBinding
    var orderId=0
    var orderApi = OrderAPI.create()
    var transportApi = TransportAPI.create()
    var orderAPI = OrderAPI.create()
    var transportList = arrayListOf<TransportDetailClass>()
    var transportTypeId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertTransportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getIntExtra("orderId", 0)
        binding.orderId.text = "Order: ${orderId}"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnAddTransport.setOnClickListener{
        }

        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        binding.rcvTransportOrder.adapter = TransportDetailAdapter(transportList, applicationContext)
        binding.rcvTransportOrder.layoutManager = LinearLayoutManager(applicationContext)
        binding.rcvTransportOrder.addItemDecoration(itemDecor)

        val transportType = resources.getStringArray(R.array.transportType)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, transportType)
        binding.autoCompleteTextViewTransport.setAdapter(arrayAdapter)

        binding.autoCompleteTextViewTransport.setOnItemClickListener { parent, _, position, _ ->
            transportTypeId = position+2
        }

        binding.btnAddTransport.setOnClickListener {
            var transportDetail = binding.editTransportDetail.text.toString()
            if(transportTypeId==0 || transportDetail.isEmpty()){
                Toast.makeText(applicationContext, "กรอกข้อมูลให้ครบครับ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            insertTransport()
            binding.editTransportDetail.text?.clear()
            binding.autoCompleteTextViewTransport.clearFocus()
            if(transportTypeId==4){
                updateReceiveSuccess()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        findPriceAll()
        findOrder()
        getTransportDetail(orderId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertTransport(){
        transportApi.insertTransport(
            binding.editTransportDetail.text.toString(),
            transportTypeId,
            orderId
        )
            .enqueue(object : Callback<TransportDetailClass> {
                override fun onResponse(call: Call<TransportDetailClass>, response: Response<TransportDetailClass>) {
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext, "เพิ่มสถานะการจัดส่งสำเร็จ", Toast.LENGTH_SHORT).show()
                        getTransportDetail(orderId)
                    }else{
                        println("cannot insert")
                    }
                }

                override fun onFailure(call: Call<TransportDetailClass>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error on orderForTransport" + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun updateReceiveSuccess(){
        transportApi.updateReceiveSuccess(orderId)
            .enqueue(object : Callback<OrderForTransport> {
                override fun onResponse(call: Call<OrderForTransport>, response: Response<OrderForTransport>) {
                    if(response.isSuccessful){
                        println("update y to table")
                    }else{
                        println("cannot update")
                    }
                }

                override fun onFailure(call: Call<OrderForTransport>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error on orderForTransport" + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getTransportDetail(orderId: Int) {
        transportList.clear()
        orderAPI.getTransportDetail(orderId)
            .enqueue(object : Callback<List<TransportDetailClass>> {
                override fun onResponse(
                    call: Call<List<TransportDetailClass>>, response:
                    Response<List<TransportDetailClass>>
                ) {
                    println(response.body())
                    response.body()?.reversed()?.forEach {
                        println(it.title)
                        if(it.title=="จัดส่งเสร็จสิ้น"){
                            binding.autoCompleteTextViewTransport.isVisible = false
                            binding.transportStatus.isVisible = false
                            binding.editTransportDetail.isVisible = false
                            binding.btnAddTransport.isEnabled = false
                            binding.btnAddTransport.isClickable = false
                            markButtonDisable(binding.btnAddTransport)
                        }
                        transportList.add(
                            TransportDetailClass(
                                it.title,
                                it.detail,
                                it.created_at,
                                it.photo
                            )
                        )

                    }
                    binding.rcvTransportOrder.adapter = TransportDetailAdapter(transportList, applicationContext)
                }

                override fun onFailure(call: Call<List<TransportDetailClass>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error on callProduct" + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
    fun markButtonDisable(button: Button) {
        button?.isEnabled = false
        button?.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
        button?.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
    }

    private fun findPriceAll(){
        orderApi.findPriceAll(orderId)
            .enqueue(object : Callback<OrderPriceAll> {
                override fun onResponse(call: Call<OrderPriceAll>, response: Response<OrderPriceAll>) {
                    if(response.isSuccessful){
                        binding.orderTotalPrice.text = "${response.body()?.price_all} THB"
                    }
                }

                override fun onFailure(call: Call<OrderPriceAll>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error on orderForTransport" + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }
    private fun findOrder(){
        orderApi.findOrder(orderId)
            .enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if(response.isSuccessful){
                        var addressId = response.body()?.user_address_id
                        orderApi.orderAddress(addressId.toString().toInt())
                            .enqueue(object : Callback<AddressClass> {
                                override fun onResponse(call: Call<AddressClass>, response: Response<AddressClass>) {
                                    println("_______________"+response.body())
                                    if(response.isSuccessful){
                                        var address = response.body()?.address
                                        var province = response.body()?.province
                                        var district = response.body()?.district
                                        var zip_code = response.body()?.zip_code
                                        var phone = response.body()?.phone
                                        binding.orderAddress.text = "${address} ${province} ${district}\n${zip_code} ${phone}"
                                    }else{
                                        println("What")
                                    }
                                }

                                override fun onFailure(call: Call<AddressClass>, t: Throwable) {
                                    Toast.makeText(applicationContext,"Error on orderForTransport" + t.message,
                                        Toast.LENGTH_LONG).show()
                                }
                            })
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error on orderForTransport" + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }

}
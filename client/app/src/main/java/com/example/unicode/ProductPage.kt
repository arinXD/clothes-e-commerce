package com.example.unicode

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.unicode.databinding.ActivityProductPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductPage : AppCompatActivity() {
    private lateinit var binding: ActivityProductPageBinding
    val productClient = ProductAPI.create()
    val orderApi = OrderAPI.create()
    val favClient = FavAPI.create()
    var sizeList = arrayListOf<SizeClass>()
    var checkSizeList = arrayListOf<String>()
    var pId: String = ""
    var orderId: Int = 0
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager(applicationContext)
        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pId = intent.getStringExtra("product_id").toString()
        val pName = intent.getStringExtra("product_name").toString()
        val pPrice = intent.getStringExtra("product_price").toString()
        val pPhoto = intent.getStringExtra("product_photo").toString()
        val pDetail = intent.getStringExtra("product_detail")
        val pAmount = intent.getStringExtra("product_amount").toString().toInt()

        println("-----------------------------------------------")
        println("product ID: "+pId)
        println("amount: "+pAmount)
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        if (pAmount==0){
            binding.btnPickUp.text = "สินค้าหมด"
            binding.btnPickUp.isEnabled = false
        }
        binding.btnplusnumber.setOnClickListener {
            var amount = (binding.amountOrder.text.toString().toInt())+1
            binding.amountOrder.setText(amount.toString())
        }
        binding.btndeletenumber.setOnClickListener {
            var amount = (binding.amountOrder.text.toString().toInt())-1
            if(amount<1) return@setOnClickListener
            binding.amountOrder.setText(amount.toString())
        }

        binding.productName.text = pName
        binding.productPrice.text = pPrice + " THB."
        binding.detail.text = pDetail
        Glide.with(this).load(pPhoto).into(binding.productImg)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rcvSize.adapter = SizeAdapter(this.sizeList, applicationContext)
        binding.rcvSize.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )

        orderApi.retrieveOrder(uId.toString().toInt())
            .enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
//                        Toast.makeText(applicationContext,"can find order", Toast.LENGTH_SHORT).show()
                        orderId = response.body()?.id!!.toInt()
                        println("orderId 0: "+orderId)
                    }else{
                        Toast.makeText(applicationContext,"cant find order id", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Order>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(
                        applicationContext,
                        "error on failed" + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        println("orderId -1: "+orderId)

        binding.btnPickUp.setOnClickListener {
            var size = ""
            if (
                !( (binding.s.isChecked) || (binding.m.isChecked) || (binding.l.isChecked) || (binding.xl.isChecked) )
            ){
                Toast.makeText(applicationContext, "เลือกไซส์", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                var selectID: Int = binding.radioGroupSize.checkedRadioButtonId
                var radioButtonChecked: RadioButton = findViewById(selectID)
                size = radioButtonChecked.text.toString()
            }

            if (pAmount==0){
                Toast.makeText(applicationContext, "สินค้าหมด", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!checkSizeList.contains(size)){
                Toast.makeText(applicationContext, "ไม่มีไซส์", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var size_id = 0
            val sizeString = size
            if (sizeString=="S"){
                size_id = 1
            }else if (sizeString=="M"){
                size_id = 2
            }else if (sizeString=="L"){
                size_id = 3
            }else if (sizeString=="XL"){
                size_id = 4
            }
            var priceAll = pPrice.toInt()*binding.amountOrder.text.toString().toInt()
            var amount = binding.amountOrder.text.toString().toInt()
            println("_________________")
            println("size Contain "+checkSizeList.contains(size))
            println("______________________________")
            println("UID "+uId.toString().toInt())
            println("amount "+amount)
            println("price "+pPrice.toInt())
            println("priceAll "+priceAll)
            println("orderId "+orderId)
            println("productId "+pId.toInt())
            println("sizeId "+size_id)

            orderApi.addOrder(
                uId.toString().toInt(),
                amount,
                pPrice.toInt(),
                priceAll,
                orderId,
                pId.toInt(),
                size_id,
            )
                .enqueue(object : Callback<OrderDetail> {
                    override fun onResponse(call: Call<OrderDetail>, response: Response<OrderDetail>) {
                        if (response.isSuccessful) {
//                            Toast.makeText(applicationContext,"Add to shop bag", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, ShoppingBag::class.java))
                            finish()
                        }else{
                            Toast.makeText(applicationContext,"Add to shop bag failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<OrderDetail>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(
                            applicationContext,
                            "error on failed" + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

        }

        binding.btnFav.setOnClickListener {
            favClient.addFav(uId.toString().toInt(),pId.toInt())
                .enqueue(object : Callback<FavProductAddClass> {
                    override fun onResponse(call: Call<FavProductAddClass>, response: Response<FavProductAddClass>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext,"Add to favorite product", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, FavoritePage::class.java))
                        }else{
                            Toast.makeText(applicationContext,"Add failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<FavProductAddClass>, t: Throwable) {
                        println(t.message)
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        pId = intent.getStringExtra("product_id").toString()
        callSize(pId.toInt())
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun callSize(pId: Int) {
        sizeList.clear()
        val productClient = ProductAPI.create()
        productClient.findProductSize(pId)
            .enqueue(object : Callback<List<SizeClass>> {
                override fun onResponse(
                    call: Call<List<SizeClass>>, response:
                    Response<List<SizeClass>>
                ) {
                    println(response.body())
                    response.body()?.reversed()?.forEach {
                        sizeList.add(
                            SizeClass(
                                it.id, it.size
                            )
                        )
                        checkSizeList.add(it.size)
                    }
                    binding.rcvSize.adapter = SizeAdapter(sizeList, applicationContext)
                }

                override fun onFailure(call: Call<List<SizeClass>>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
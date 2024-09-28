package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.unicode.databinding.ActivityAdminEditProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminEditProduct : AppCompatActivity() {
    private lateinit var bindingEdit : ActivityAdminEditProductBinding
    val createClient = AdminProductAPI.create()
    var productTypeListObj = ArrayList<String>()
    var productSizeList = ArrayList<String>()
    var addSize = ArrayList<Int>()
    var deleteSize = ArrayList<Int>()
    var mId : Int = 0
    var type_id: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingEdit = ActivityAdminEditProductBinding.inflate(layoutInflater)
        setContentView(bindingEdit.root)

        mId = intent.getStringExtra("id").toString().toInt()
        println("_________\n from get intent")
        println(mId)
        val mName = intent.getStringExtra("product_name")
        val mPrice = intent.getIntExtra("price",0)

        val mDetail = intent.getStringExtra("detail")
        val mPhoto = intent.getStringExtra("photo")
        val mAmount = intent.getIntExtra("amount",0)

        bindingEdit.edtNameProduct.setText(mName)
        bindingEdit.edtPriceProduct.setText(mPrice.toString())
        bindingEdit.edtDetailProduct.setText(mDetail)
        bindingEdit.edtImage.setText(mPhoto.toString())
        bindingEdit.edtSizeS.setText(mAmount.toString())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productTypeListObj)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bindingEdit.autoCompleteTextViewProduct.setAdapter(arrayAdapter)

        bindingEdit.autoCompleteTextViewProduct.setOnItemClickListener{ parent, _, position, _->
            type_id = position+1
        }

        bindingEdit.btnAddProduct.setOnClickListener{
            if(bindingEdit.autoCompleteTextViewProduct.text.toString() =="ประเภทสินค้า"){
                Toast.makeText(applicationContext, "เลือกประเภทสินค้า", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(bindingEdit.cdS.isChecked==true) {
                addSize.add(1)
            }else{
                deleteSize.add(1)
            }
            if(bindingEdit.cdM.isChecked==true) {
                addSize.add(2)
            }else{
                deleteSize.add(2)
            }
            if(bindingEdit.cdL.isChecked==true) {
                addSize.add(3)
            }else{
                deleteSize.add(3)
            }
            if(bindingEdit.cdXL.isChecked==true) {
                addSize.add(4)
            }else{
                deleteSize.add(4)
            }
            println(addSize)
            println(deleteSize)
            println(type_id)

            saveProduct()
            addSize(addSize)
            delSize(deleteSize)
        }
    }

    override fun onResume() {
        super.onResume()
        findSize()
        getAllType()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home)
        { finish() }
        return super.onOptionsItemSelected(item)
    }
    fun saveProduct() {
        createClient.updateProduct(
            mId,
            bindingEdit.edtNameProduct.text.toString(),
            bindingEdit.edtPriceProduct.text.toString().toInt(),
            bindingEdit.edtDetailProduct.text.toString(),
            bindingEdit.edtImage.text.toString(),
            bindingEdit.edtSizeS.text.toString().toInt(),
            type_id,
        ).enqueue(object : Callback<AdminProduct> {
            override fun onResponse(call: Call<AdminProduct>, response: Response<AdminProduct>) {
                if(response.isSuccessful) {
                    Toast.makeText(applicationContext,"Successfully Updated",
                        Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Update Failure",
                        Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AdminProduct>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure "+ t.message,
                    Toast.LENGTH_LONG).show()
            }
        })
    }
//            addSize,
//            deleteSize
    fun addSize(addSize: ArrayList<Int>){
        addSize.forEach{
            createClient.addProductSize(mId, it).enqueue(object : Callback<SizeClass> {
                override fun onResponse(call: Call<SizeClass>, response: Response<SizeClass>) {
                    if(response.isSuccessful) {
                        ""
                    } else {
                        Toast.makeText(applicationContext, "insert Failure",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<SizeClass>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure "+ t.message,Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    fun delSize(delSize: ArrayList<Int>){
        delSize.forEach{
            createClient.deleteProductSize(mId, it).enqueue(object : Callback<SizeClass> {
                override fun onResponse(call: Call<SizeClass>, response: Response<SizeClass>) {
                    if(response.isSuccessful) {
                        ""
                    } else {
                        Toast.makeText(applicationContext, "del Failure",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<SizeClass>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure "+ t.message,Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    fun getAllType(){
        createClient.getAllType().enqueue(object : Callback<List<ProductType>> {
            override fun onResponse(call: Call<List<ProductType>>, response:

            Response<List<ProductType>>) {

                response.body()?.forEach {
                    var type = ProductType(it.id, it.type_name)
                    productTypeListObj.add(type.type_name)

                }
            }
            override fun onFailure(call: Call<List<ProductType>>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    fun findSize(){
        createClient.findProductSize(mId).enqueue(object : Callback<List<SizeClass>> {
            override fun onResponse(call: Call<List<SizeClass>>, response: Response<List<SizeClass>>) {
                if(response.isSuccessful) {
                    println(response.body())
                    response.body()?.forEach {
                        var size = SizeClass(it.id, it.size)
                        productSizeList.add(size.size)
                    }
                    println(productSizeList)
                    productSizeList.forEach{
                        if(it=="S") {bindingEdit.cdS.isChecked=true}
                        if(it=="M") {bindingEdit.cdM.isChecked=true}
                        if(it=="L") {bindingEdit.cdL.isChecked=true}
                        if(it=="XL") {bindingEdit.cdXL.isChecked=true}
                    }

                } else {
                    Toast.makeText(applicationContext, "Update Failure",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<SizeClass>>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure "+ t.message,Toast.LENGTH_LONG).show()
            }
        })
    }
}
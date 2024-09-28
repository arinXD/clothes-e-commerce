package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.unicode.databinding.ActivityAdminInsertProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminInsertProduct : AppCompatActivity() {
    private lateinit var bindingInsertProduct : ActivityAdminInsertProductBinding
    private val api = AdminProductAPI.create()
    var productTypeListObj = ArrayList<String>()
    var type_id: Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingInsertProduct = ActivityAdminInsertProductBinding.inflate(layoutInflater)
        setContentView(bindingInsertProduct.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productTypeListObj)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bindingInsertProduct.dropdownInsert.setAdapter(arrayAdapter)

        bindingInsertProduct.dropdownInsert.setOnItemClickListener{ parent, _, position, _->
            type_id = position+1
        }

        bindingInsertProduct.btnAddProduct.setOnClickListener {
            println("______\ntype id")
            println(type_id)
            addProduct(type_id)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllType()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home)
        { finish() }
        return super.onOptionsItemSelected(item)
    }

    private fun addProduct(typeId: Int) {
        api.insertProduct(
            bindingInsertProduct.edtNameProduct.text.toString(),
            bindingInsertProduct.edtPriceProduct.text.toString().toInt(),
            bindingInsertProduct.edtDetailProduct.text.toString(),
            bindingInsertProduct.edtImage.text.toString(),
            bindingInsertProduct.edtSizeS.text.toString().toInt(),
            typeId
        ).enqueue(object: Callback<AdminProduct> {
            override fun onResponse(
                call: Call<AdminProduct>,
                response: Response<AdminProduct>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,"Successfully Inserted",
                        Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext,"Inserted Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AdminProduct>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure "+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getAllType(){
        api.getAllType().enqueue(object : Callback<List<ProductType>> {
            override fun onResponse(call: Call<List<ProductType>>, response:

            Response<List<ProductType>>) {

                response.body()?.forEach {
                    var type = ProductType(it.id, it.type_name)
                    productTypeListObj.add(type.type_name)

                }
//                binding.rcv.adapter = ProductsAdapter(productsList, applicationContext)
            }
            override fun onFailure(call: Call<List<ProductType>>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    fun callprotype(onSuccess: (List<producttypeClass>) -> Unit, onFailure: (Throwable) -> Unit) {
        api.retrieveProductType().enqueue(object : Callback<List<producttypeClass>> {
            override fun onResponse(call: Call<List<producttypeClass>>, response: Response<List<producttypeClass>>) {
                if (response.isSuccessful) {
                    val van = response.body()
                    if (van != null) {
                        onSuccess(van)
                    }
                } else {
                    onFailure(Throwable("Failed to fetch cities"))
                }
            }

            override fun onFailure(call: Call<List<producttypeClass>>, t: Throwable) {
                onFailure(t)
            }
        })

    }
}
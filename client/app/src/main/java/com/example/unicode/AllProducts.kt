package com.example.unicode

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unicode.databinding.ActivityAllProductsBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllProducts : AppCompatActivity() {

//    attribute
    private lateinit var binding: ActivityAllProductsBinding
    var productsList = arrayListOf<ProductClass>()
    lateinit var session: SessionManager
    var typeId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAllProductsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        session = SessionManager(applicationContext)

//        val username: String? = session.pref.getString(SessionManager.KEY_NAME, null)
//        val id: String? = session.pref.getString(SessionManager.KEY_ID, null)

        var userData = intent.getStringArrayListExtra("userData")
        println(userData)

        binding.rcv.layoutManager = GridLayoutManager(this,2)
        binding.rcv.addItemDecoration(DividerItemDecoration(
            binding.rcv.getContext(),DividerItemDecoration.VERTICAL)
        )


        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId){
                R.id.category -> startActivity(Intent(applicationContext, CategoryPage::class.java))
                R.id.home ->{
                    ""
                }
                R.id.account -> {
                    intent = Intent(applicationContext,AccountPage::class.java)
                    intent.putStringArrayListExtra("userData",userData)
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        typeId = intent.getIntExtra("typeId",0)
        println(typeId)
        productLast()
        callProductsData()
    }

    //    create menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav -> {
                var intent = Intent(applicationContext, FavoritePage::class.java)
                startActivity(intent)
            }
            R.id.basket -> {
                var intent = Intent(applicationContext, ShoppingBag::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    method
    fun callProductsData(){
        productsList.clear()
        val productClient = ProductAPI.create()
        productClient.productTypeAll(typeId).enqueue(object : Callback<List<ProductClass>> {
                override fun onResponse(call: Call<List<ProductClass>>, response:

                Response<List<ProductClass>>) {

                    response.body()?.forEach {
                        productsList.add(ProductClass(
                            it.id, it.product_name,it.price,it.detail,it.photo,it.amount,it.subtype_id))
                    }
                    binding.rcv.adapter = ProductsAdapter(productsList, applicationContext)
                }
                override fun onFailure(call: Call<List<ProductClass>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    fun productLast(){
        val serv : ProductAPI = Retrofit.Builder() // Create Client
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductAPI ::class.java)
        serv.productLast()
            .enqueue(object : Callback<List<ProductClass>> {
                override fun onResponse(call: Call<List<ProductClass>>, response:
                Response<List<ProductClass>>) {
                    response.body()?.forEach {
                        val uri = it.photo
                        Picasso.get().load(uri).into(binding.lastProduct)
                    }
                }
                override fun onFailure(call: Call<List<ProductClass>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

}
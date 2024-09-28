package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityFavoritePageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritePage : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritePageBinding
    var productsList = arrayListOf<FavProduct>()
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFavoritePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        session = SessionManager(applicationContext)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDispla

        binding.rcv.adapter = FavoriteAdapter(productsList,applicationContext)
        binding.rcv.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        binding.rcv.addItemDecoration(itemDecor)


        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
//                R.id.category -> Toast.makeText(applicationContext, "Category", Toast.LENGTH_LONG)
//                    .show()
                R.id.home -> {
                    intent = Intent(applicationContext, AllProducts::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
                    intent = Intent(applicationContext, AccountPage::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        allFavorite(uId.toString().toInt())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun allFavorite(id: Int) {
        productsList.clear()
        val favClient = FavAPI.create()
        favClient.favProduct(id)
            .enqueue(object : Callback<List<FavProduct>> {
                override fun onResponse(
                    call: Call<List<FavProduct>>, response:
                    Response<List<FavProduct>>
                ) {
                    println(response.body())
                    response.body()?.forEach {
                        productsList.add(FavProduct(it.pv_id,it.id,it.product_name, it.detail,it.price,it.photo ,it.amount))
                    }
                    binding.rcv.adapter = FavoriteAdapter(productsList, applicationContext)
                }

                override fun onFailure(call: Call<List<FavProduct>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
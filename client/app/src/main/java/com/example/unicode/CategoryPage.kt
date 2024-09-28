package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityCategoryPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryPage : AppCompatActivity() {
    private lateinit var bd: ActivityCategoryPageBinding
    var categoryList = arrayListOf<CategoryClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        bd = ActivityCategoryPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bd.root)

        bd.rcv.adapter = CategoryAdapter(categoryList,applicationContext)
        bd.rcv.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        bd.rcv.addItemDecoration(itemDecor)

        bd.btnClearCat.setOnClickListener {
            var i = Intent(applicationContext,AllProducts::class.java)
            i.putExtra("typeId",0)
            startActivity(i)
        }

        bd.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.category -> ""
                R.id.home -> startActivity(Intent(applicationContext,AllProducts::class.java))
                R.id.account -> startActivity(Intent(applicationContext,AccountPage::class.java))
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        callCategory()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callCategory(){
        var api = ProductAPI.create()
        api.getAllProductType()
            .enqueue(object : Callback<List<CategoryClass>> {
                override fun onResponse(
                    call: Call<List<CategoryClass>>, response:
                    Response<List<CategoryClass>>
                ) {
                    println(response.body())
                    response.body()?.forEach {
                        categoryList.add(CategoryClass(it.id, it.type_name, it.type_photo))
                    }
                    bd.rcv.adapter = CategoryAdapter(categoryList, applicationContext)
                }

                override fun onFailure(call: Call<List<CategoryClass>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.unicode.databinding.ActivityCompleteStatusPageBinding

class CompleteStatusPage : AppCompatActivity() {

    private lateinit var binding: ActivityCompleteStatusPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCompleteStatusPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(applicationContext, OrderHistory::class.java))
        }
        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId){
//                R.id.category -> Toast.makeText(applicationContext, "Category", Toast.LENGTH_LONG).show()
                R.id.home ->{
                    startActivity(Intent(applicationContext, AllProducts::class.java))
                }
                R.id.account -> {
                    intent = Intent(applicationContext,AccountPage::class.java)
                    startActivity(intent)
                }
            }
            true
        }
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
}
package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.unicode.databinding.ActivityAdminSelectTransportMangeBinding

class AdminSelectTransportMange : AppCompatActivity() {
    private lateinit var binding: ActivityAdminSelectTransportMangeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminSelectTransportMangeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSuccessful.setOnClickListener {
            var i = Intent(applicationContext, TransportPage::class.java)
            i.putExtra("receiveStatus", "Y")
            startActivity(i)
        }
        binding.btnUnsuccessful.setOnClickListener {
            var i = Intent(applicationContext, TransportPage::class.java)
            i.putExtra("receiveStatus", "N")
            startActivity(i)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
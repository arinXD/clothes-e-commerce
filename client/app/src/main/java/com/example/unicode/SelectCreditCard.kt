package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivitySelectCreditCardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectCreditCard : AppCompatActivity() {
    private lateinit var binding: ActivitySelectCreditCardBinding
    var creditList = arrayListOf<Credit>()
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySelectCreditCardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        session = SessionManager(applicationContext)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rcv.adapter = SelectCreditAdapter(creditList, applicationContext)
        binding.rcv.layoutManager = LinearLayoutManager(applicationContext)
        binding.rcv.addItemDecoration(
            DividerItemDecoration(binding.rcv.getContext(),
                DividerItemDecoration.VERTICAL)
        )
        binding.btnAddCredit.setOnClickListener {
            startActivity(Intent(applicationContext, InsertCradit::class.java))
        }
    }
    override fun onResume() {
        super.onResume()
        callCreditData()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun callCreditData(){
        creditList.clear()
        val serv = CraditAPI.create()
        val id: String? = session.pref.getString(SessionManager.KEY_ID, null)
        serv.myCredit(id.toString().toInt())
            .enqueue(object : Callback<List<Credit>> {
                override fun onResponse(call: Call<List<Credit>>, response: Response<List<Credit>>) {
                    response.body()?.forEach{
                        creditList.add(Credit(it.id,it.card_no,it.expire_date,it.cvv,it.firstname))
                    }
                    binding.rcv.adapter = SelectCreditAdapter(creditList,applicationContext)

                }
                override fun onFailure(call: Call<List<Credit>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error on callCredit()", Toast.LENGTH_LONG).show()
                }
            })
    }
}
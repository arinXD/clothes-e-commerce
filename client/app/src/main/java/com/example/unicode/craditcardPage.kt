package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityCraditcardPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class craditcardPage : AppCompatActivity() {
    private lateinit var binding: ActivityCraditcardPageBinding
    var craditlist = arrayListOf<Credit>()
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCraditcardPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        session = SessionManager(applicationContext)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerViewCradit.adapter = CraditAdapter(this.craditlist, applicationContext)
        binding.recyclerViewCradit.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewCradit.addItemDecoration(
            DividerItemDecoration(binding.recyclerViewCradit.getContext(),
                DividerItemDecoration.VERTICAL)
        )

        binding.btnAddCradit.setOnClickListener {
            val intent = Intent(applicationContext,InsertCradit::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val id: String? = session.pref.getString(SessionManager.KEY_ID, null)
        callCreditData(id.toString().toInt())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callCreditData(id: Int){
        craditlist.clear()
        val serv = CraditAPI.create()

        serv.myCredit(id)
            .enqueue(object : Callback<List<Credit>> {
                override fun onResponse(call: Call<List<Credit>>, response: Response<List<Credit>>) {
                    response.body()?.forEach{
                        craditlist.add(Credit(it.id,it.card_no,it.expire_date,it.cvv,it.firstname))
                    }
                    binding.recyclerViewCradit.adapter = CraditAdapter(craditlist,applicationContext)

                }


                override fun onFailure(call: Call<List<Credit>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailue " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }
}
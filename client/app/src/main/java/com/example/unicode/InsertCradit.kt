package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.unicode.databinding.ActivityInsertCraditBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InsertCradit : AppCompatActivity() {
    private lateinit var bindingInsert: ActivityInsertCraditBinding
    var craditlist = arrayListOf<Credit>()
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(applicationContext)
        bindingInsert = ActivityInsertCraditBinding.inflate(layoutInflater)
        setContentView(bindingInsert.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindingInsert.btnAddCradit.setOnClickListener {
            val id: String? = session.pref.getString(SessionManager.KEY_ID, null)
            addCradit(id.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDatePickerDialog(v: View) {
        val newDateFragment = DatePickerCredit()
        newDateFragment.show(supportFragmentManager, "Date Picker")
    }

    private fun addCradit(id: String) {
        val api: CraditAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CraditAPI::class.java)
        api.insertCradit(
            bindingInsert.editName.text.toString(),
            bindingInsert.editCardID.text.toString(),
            bindingInsert.editexpiredate.text.toString(),
            bindingInsert.editcvv.text.toString().toInt(),
            id.toInt()
        ).enqueue(object : Callback<Credit> {
            override fun onResponse(
                call: Call<Credit>,
                response: retrofit2.Response<Credit>
            ) {
                if (response.isSuccessful()) {
                    Toast.makeText(applicationContext, "Successfully Inserted", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Inserted Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Credit>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Error onFailuse " + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
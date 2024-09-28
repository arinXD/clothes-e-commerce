package com.example.unicode

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityAddressPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class addressPage : AppCompatActivity() {

    private lateinit var binding: ActivityAddressPageBinding
    var addresslist = arrayListOf<Address>()
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddressPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        session = SessionManager(applicationContext)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerView.adapter = AddressAdapter(this.addresslist, applicationContext)
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(binding.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL)
        )
        binding.btnAddAddress.setOnClickListener{
            addAddress()
        }
    }

    override fun onResume() {
        super.onResume()
        callAddressData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callAddressData(){
        addresslist.clear()
        val serv : AddressAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AddressAPI ::class.java)
        val id: String? = session.pref.getString(SessionManager.KEY_ID, null)
        serv.retrieveAddress(id.toString().toInt())
            .enqueue(object : Callback<List<Address>> {
                override fun onResponse(call: Call<List<Address>>, response: Response<List<Address>>) {
                    response.body()?.forEach{
                        addresslist.add(Address(it.id,it.address,it.province,it.district,it.zip_code,it.phone))
                    }
                    binding.recyclerView.adapter = AddressAdapter(addresslist,applicationContext)

                }

                override fun onFailure(call: Call<List<Address>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailue " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }
    @SuppressLint("MissingInflatedId")

    fun addAddress(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.address_dialog_layout,null)
        val myBuilder = AlertDialog.Builder(this)

        val api = AddressAPI.create()
        myBuilder.setView(mDialogView)
        ///save Button
        myBuilder.setNegativeButton("Save"){dialog, _ ->
            val addressEditText = mDialogView.findViewById(R.id.editaddress) as EditText
            val provinceEditText = mDialogView.findViewById(R.id.editprovince) as EditText
            val districtEditText = mDialogView.findViewById(R.id.editdistrict) as EditText
            val zipCodeEditText = mDialogView.findViewById(R.id.editzip_code) as EditText
            val phoneEditText = mDialogView.findViewById(R.id.editphone) as EditText

            val address = addressEditText.text.toString()
            val province = provinceEditText.text.toString()
            val district = districtEditText.text.toString()
            val zipCode = zipCodeEditText.text.toString()
            val phone = phoneEditText.text.toString().toInt()
            val id: String? = session.pref.getString(SessionManager.KEY_ID, null)

            api.insertAddr(address, province, district, zipCode, phone,id.toString().toInt())
                .enqueue(object : Callback<Address> {
                    override fun onResponse(
                        call: Call<Address>,
                        response: Response<Address>
                    ) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                applicationContext,
                                "Successfully Inserted",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                            callAddressData()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Inserted Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Address>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Error onFailuse " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

        }
        ///Cancel Button
        myBuilder.setPositiveButton("Cancel",){dialog, which->
            dialog.dismiss()
        }
        myBuilder.show()
    }
}
package com.example.unicode

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.SelectAddressItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectAddressAdapter(val addresslist: ArrayList<Address>, val context: Context) :
    RecyclerView.Adapter<SelectAddressAdapter.ViewHolder>() {

    inner class ViewHolder(view: View, val binding: SelectAddressItemBinding) :
        RecyclerView.ViewHolder(view) {
        lateinit var session: SessionManager
        val orderApi = OrderAPI.create()
        var orderId: Int = 0

        init {
            session = SessionManager(itemView.context)
            val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
            orderApi.retrieveOrder(uId.toString().toInt()).enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
                        orderId = response.body()?.id!!.toInt()
                    } else {
                        Toast.makeText(context, "cant find order id", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(context, "error on failed" + t.message, Toast.LENGTH_LONG).show()
                }
            })
            binding.btnSelectAddress.setOnClickListener {
                val item = addresslist[adapterPosition]
                var addressId = item.id.toString().toInt()


                println("____________________")
                println("order:"+orderId+" address:"+addressId)
                orderApi.updateOrderAddress(orderId, addressId).enqueue(object : Callback<Order> {
                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        if (response.isSuccessful) {
//                            Toast.makeText(itemView.context, "Add address success", Toast.LENGTH_SHORT).show()
                            (itemView.context as Activity).finish()
                        } else {
                            Toast.makeText(context, "cant find address id", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Order>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(context, "error on failed" + t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SelectAddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = SessionManager(context)
        val name: String? = session.pref.getString(SessionManager.KEY_NAME, null)
        val binding = holder.binding
        val currentItem = addresslist!![position]

        binding.userName.text = name
        binding.address.text = "Address: " + currentItem.address
        binding.province.text = "Province: " + currentItem.province
        binding.district.text = "District: " + currentItem.district
        binding.zipCode.text = "ZipCode: " + currentItem.zip_code
        binding.phone.text = "Phone: " + currentItem.phone

    }

    override fun getItemCount(): Int {
        return addresslist?.size ?: 0
    }
}
package com.example.unicode

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.SelectCreditItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SelectCreditAdapter (val creditList : ArrayList<Credit>, val context: Context) :
    RecyclerView.Adapter<SelectCreditAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: SelectCreditItemBinding) :
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
                val item = creditList[adapterPosition]
                var creditId = item.id.toString().toInt()


                println("____________________")
                println("order:"+orderId+" creditId:"+creditId)
                orderApi.updateOrderCredit(orderId, creditId).enqueue(object : Callback<Order> {
                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        if (response.isSuccessful) {
//                            Toast.makeText(itemView.context, "Add credit success", Toast.LENGTH_SHORT).show()
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
        val binding = SelectCreditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val currentItem = creditList!![position]
        binding.userName.text = currentItem.firstname
        binding.cardNo.text = "Card No: " + getMaskedCardNumber(currentItem.card_no.toString())
        binding.expireDate.text = "Expire Data: " + formatDate(currentItem.expire_date)
        binding.cvv.text = "CVV: " + getMaskedCVV(currentItem.cvv)
    }

    private fun getMaskedCardNumber(cardNumber: String): String {
        // Replace all but the last 4 digits with asterisks
        val masked = cardNumber.replace(Regex("\\d(?=\\d{4})"), "*")
        return masked
    }

    private fun getMaskedCVV(cvv: Int): String {
        // Create a string of asterisks with the same length as the CVV number
        val masked = "*".repeat(cvv.toString().length)
        return masked
    }
    private fun formatDate(dateString: String): String {
        // Parse the date string and format it to "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    override fun getItemCount(): Int {
        return creditList?.size ?: 0
    }

}
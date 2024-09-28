package com.example.unicode

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.CraditItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CraditAdapter (val craditlist : ArrayList<Credit>, val context: Context) :
    RecyclerView.Adapter<CraditAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: CraditItemBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            binding.deleteitem.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Delete Confirmation")
                builder.setMessage("Are you sure you want to delete this data?")
                builder.setNegativeButton("Yes") { dialog, which ->
                    val position = adapterPosition
                    val addressId = craditlist[position].id
                    val serv = CraditAPI.create()
                    serv.deleteCradit(addressId).enqueue(object : retrofit2.Callback<Credit> {
                        override fun onResponse(
                            call: retrofit2.Call<Credit>,
                            response: retrofit2.Response<Credit>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "Seccessfully Delete",
                                    Toast.LENGTH_LONG
                                ).show()
                                craditlist.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<Credit>, t: Throwable) {
                            Toast.makeText(
                                context, "Update Failure",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
                builder.setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CraditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val currentItem = craditlist!![position]
        binding.Namenaja.text = currentItem.firstname
        binding.Cardno.text = "Card No: " + getMaskedCardNumber(currentItem.card_no.toString())
        binding.expiredate.text = "Expire Data: " + formatDate(currentItem.expire_date)
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
        return craditlist?.size ?: 0
    }

}
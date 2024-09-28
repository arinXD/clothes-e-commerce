package com.example.unicode

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unicode.databinding.ShoppingItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShoppingBagAdapter (val items : ArrayList<OrderProductClass>, val context: Context) :
    RecyclerView.Adapter<ShoppingBagAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: ShoppingItemBinding):
        RecyclerView.ViewHolder(view){
        init{
            binding.btnDelete.setOnClickListener {
                val item = items[adapterPosition]
                val id = item.order_datial_id
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("ตะกร้าสินค้า")
                builder.setMessage("ต้องการลบสินค้าจากรายการ?")
                builder.setNegativeButton("Yes"){ dialog, _ ->
                    val position = adapterPosition
                    items.removeAt(position)
                    notifyDataSetChanged()
                    deleteOrder(id)
                    itemView.context.startActivity(Intent(context, ShoppingBag::class.java))
                    (itemView.context as Activity).finish()
                }
                builder.setPositiveButton("No"){ dialog, _ ->
                    dialog.dismiss()

                }
                builder.show()
            }//btn
        }//init

    }
    fun deleteOrder(id: Int) {
        var client = OrderAPI.create()
        client.deleteOrderDetail(id).enqueue(object : Callback<OrderProductClass> {
            override fun onResponse(call: Call<OrderProductClass>, response: Response<OrderProductClass>) {
                if (response.isSuccessful) {
//                    Toast.makeText(context, "Successfully Delete",Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<OrderProductClass>, t: Throwable) {
                Toast.makeText(
                    context, "Delete Failure",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagAdapter.ViewHolder {
        val binding = ShoppingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ShoppingBagAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

        Glide.with(context).load(items[position].photo).into(binding_holder.productImg)
        binding_holder.productName?.text = items[position].product_name
        binding_holder.productDetail?.text = items[position].detail
        binding_holder.productPrice?.text = String.format("%,d", items[position].product_price) + " THB."
        binding_holder.productSize?.text = items[position].size
        binding_holder.productAmount?.text = items[position].amount.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

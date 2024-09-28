package com.example.unicode

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unicode.databinding.ShoppingDetailItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailShoppingBagAdapter (val items : ArrayList<OrderDetailShopBagProduct>, val context: Context) :
    RecyclerView.Adapter<OrderDetailShoppingBagAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: ShoppingDetailItemBinding):
        RecyclerView.ViewHolder(view){
        init{
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailShoppingBagAdapter.ViewHolder {
        val binding = ShoppingDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: OrderDetailShoppingBagAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

//        Glide.with(context).load(items[position].photo).into(binding_holder.productImg)
        binding_holder.productName?.text = items[position].product_name
        binding_holder.productAmount?.text = items[position].amount.toString()
        binding_holder.productPrice?.text = String.format("%,d", items[position].price)
        binding_holder.totalPrice?.text = items[position].priceAll.toString() + " THB."
//        binding_holder.productAmount?.text = items[position].amount.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
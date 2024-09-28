package com.example.unicode

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.OrderForTransportLayoutBinding

class OrderForTransportAdapter (val items : ArrayList<OrderForTransport>, val context: Context) :
    RecyclerView.Adapter<OrderForTransportAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: OrderForTransportLayoutBinding):
        RecyclerView.ViewHolder(view){
        init{
            binding.btnSelectOrder.setOnClickListener {
                var item = items[adapterPosition]
                var orderId = item.id
                var i = Intent(context, InsertTransport::class.java)
                i.putExtra("orderId", orderId)
                view.context.startActivity(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderForTransportAdapter.ViewHolder {
        val binding = OrderForTransportLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: OrderForTransportAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

        binding_holder.orderId.text = "Order : ${items[position].id}"
        binding_holder.orderCreated.text = "สั่งวันที่ : ${DateFormatUnicode.formatDate(items[position].created_at)} "
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
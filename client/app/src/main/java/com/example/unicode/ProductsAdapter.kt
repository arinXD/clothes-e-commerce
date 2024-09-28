package com.example.unicode

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unicode.databinding.ProductItemLayoutBinding

class ProductsAdapter(val items : ArrayList<ProductClass>, val context: Context) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: ProductItemLayoutBinding):
        RecyclerView.ViewHolder(view){
            init{
                binding.cardView.setOnClickListener {
                    val item = items[adapterPosition]
                    val contextView : Context = view.context
                    val intent = Intent(context, ProductPage::class.java)
                    intent.putExtra("product_id",item.id.toString())
                    intent.putExtra("product_detail",item.detail)
                    intent.putExtra("product_name",item.product_name)
                    intent.putExtra("product_price",item.price.toString())
                    intent.putExtra("product_photo",item.photo)
                    intent.putExtra("product_amount",item.amount.toString())
//                    println("-----------------------------------------------")
//                    println(item.amount.toString())
                    contextView.startActivity(intent)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ViewHolder {
        val binding = ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

        binding_holder.productName?.text = items[position].product_name.toString()
        binding_holder.price?.text = String.format("%,d", items[position].price) + " THB."
        Glide.with(context).load(items[position].photo).into(binding_holder.imageProduct)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
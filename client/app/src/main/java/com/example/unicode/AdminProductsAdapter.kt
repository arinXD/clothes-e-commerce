package com.example.unicode

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unicode.databinding.AdminProductItemLayoutBinding

class AdminProductsAdapter(
    val productList: ArrayList<AdminProduct>?,
//    val items:List<Product>,
    val context: Context, val listener: MyClickListener
) :
    RecyclerView.Adapter<AdminProductsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View, val binding: AdminProductItemLayoutBinding) :
        RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val position = adapterPosition
                listener.onClick(position)

//                    val item = items[adapterPosition]
//                    val contextView: Context = view.context
//                    val intent = Intent(contextView, ShowEditDeleteActivity::class.java)
//                    intent.putExtra("mName", item.product_name)
//                    intent.putExtra("mPrice", item.price.toString())
//                    intent.putExtra("mDetail", item.detail)
//                    intent.putExtra("mPhoto", item.photo)
//                    intent.putExtra("mAmount", item.amount.toString())
//                    intent.putExtra("mSubtype_id", item.subtype_id.toString())
//                    contextView.startActivity(intent)
            }
        }
    }

    interface MyClickListener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdminProductItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        Glide.with(context).load(productList!![position].photo).into(binding.imageProduct)
        binding.title.text = "" + productList!![position].product_name
        binding.price.text = "฿" + productList!![position].price.toString()

    }

    override fun getItemCount(): Int {
        return productList!!.size
    }
}
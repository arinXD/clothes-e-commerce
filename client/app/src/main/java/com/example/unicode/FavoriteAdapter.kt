package com.example.unicode

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unicode.databinding.FavoriteItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteAdapter (val items : ArrayList<FavProduct>, val context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: FavoriteItemBinding):
        RecyclerView.ViewHolder(view){
        init{
            var favClient = FavAPI.create()
            binding.btnDeleteItem.setOnClickListener {
                val item = items[adapterPosition]
                val pv_id = item.pv_id
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("รายการโปรด")
                builder.setMessage("ต้องการลบสินค้าจากรายการโปรด?")
                builder.setNegativeButton("Yes"){ dialog, _ ->
                    val position = adapterPosition
                    items.removeAt(position)
                    notifyDataSetChanged()
                    deleteFavProduct(pv_id)
                }
                builder.setPositiveButton("No"){ dialog, _ ->
                    dialog.dismiss()

                }
                builder.show()

            }
            binding.btnAddtoCart.setOnClickListener {
                println(":DDDDDDD")
                val item = items[adapterPosition]
                val contextView : Context = view.context
                val intent = Intent(context, ProductPage::class.java)
                intent.putExtra("product_id",item.id.toString())
                intent.putExtra("product_detail",item.detail)
                intent.putExtra("product_name",item.product_name)
                intent.putExtra("product_price",item.price.toString())
                intent.putExtra("product_photo",item.photo)
                intent.putExtra("product_amount",item.amount.toString())
                contextView.startActivity(intent)
            }
        }
    }
    fun deleteFavProduct(pv_id: Int) {
        var favClient = FavAPI.create()
        favClient.deleteFav(pv_id).enqueue(object : Callback<FavProduct> {
            override fun onResponse(call: Call<FavProduct>, response: Response<FavProduct>) {
                if (response.isSuccessful) {
                }
            }
            override fun onFailure(call: Call<FavProduct>, t: Throwable) {
                Toast.makeText(
                    context, "Update Failure",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

        binding_holder.favProductName?.text = items[position].product_name
        binding_holder.favProductPrice?.text = String.format("%,d", items[position].price) + " THB."
        Glide.with(context).load(items[position].photo).into(binding_holder.favProductImg)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
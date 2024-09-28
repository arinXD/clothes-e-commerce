package com.example.unicode

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.CategoryItemLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryAdapter (val items : ArrayList<CategoryClass>, val context: Context) :
RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: CategoryItemLayoutBinding):
        RecyclerView.ViewHolder(view){
        init{
            binding.categoryName.setOnClickListener {
                var type_id = items[adapterPosition]
//                Toast.makeText(context, ""+type_id, Toast.LENGTH_SHORT).show()
                var intent = Intent(context, AllProducts::class.java)
                intent.putExtra("typeId", type_id.id)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val binding = CategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding
        binding_holder.categoryName.text = items[position].type_name
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
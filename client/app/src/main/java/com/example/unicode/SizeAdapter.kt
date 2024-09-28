package com.example.unicode

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.SizeItemLayoutBinding

class SizeAdapter(val items : ArrayList<SizeClass>, val context: Context) :
    RecyclerView.Adapter<SizeAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: SizeItemLayoutBinding):
        RecyclerView.ViewHolder(view){
        init{

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapter.ViewHolder {
        val binding = SizeItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: SizeAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding
        binding_holder.sizeText?.text = items[position].size.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
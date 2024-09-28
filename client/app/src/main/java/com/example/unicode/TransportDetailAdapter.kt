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
import com.example.unicode.databinding.TransportDetailItemLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransportDetailAdapter (val items : ArrayList<TransportDetailClass>, val context: Context) :
    RecyclerView.Adapter<TransportDetailAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: TransportDetailItemLayoutBinding):
        RecyclerView.ViewHolder(view){
        init{

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransportDetailAdapter.ViewHolder {
        val binding = TransportDetailItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: TransportDetailAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

        Glide.with(context).load(items[position].photo).into(binding_holder.transportImg)
        binding_holder.transportTitle?.text = items[position].title
        binding_holder.transportCreate?.text = DateFormatUnicode.formatDate(items[position].created_at)
        binding_holder.transportDetailHis?.text = items[position].detail
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
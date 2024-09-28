package com.example.unicode

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unicode.databinding.TransportItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransportAdapter(val transportlist : ArrayList<Order>, val context: Context) :
    RecyclerView.Adapter<TransportAdapter.ViewHolder>(){
    class ViewHolder(view: View, val binding: TransportItemBinding) :
        RecyclerView.ViewHolder(view) {
            init {
                binding.btnUpdateTrasport.setOnClickListener {
                    val contextView : Context = view.context
                    val intent = Intent(contextView,InsertTransport::class.java)
                    contextView.startActivity(intent)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val binding = TransportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val currentItem = transportlist!![position]
        binding.textlist.text = "รายการที่: "+transportlist!![position].id.toString()
        binding.dateTransport.text = "ส่งวันที่: "+formatDate(currentItem.created_at)
    }
    private fun formatDate(dateString: String): String {
        // Parse the date string and format it to "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    override fun getItemCount(): Int {
        return transportlist?.size ?: 0
    }
}
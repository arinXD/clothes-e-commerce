package com.example.unicode

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
//import com.example.unicode.address
import com.example.unicode.databinding.AddressItemBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddressAdapter(val addresslist : ArrayList<Address>, val context: Context) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    inner class ViewHolder(view: View, val binding: AddressItemBinding) :
        RecyclerView.ViewHolder(view) {
        init {

            binding.deleteitem.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Delete Confirmation")
                builder.setMessage("Are you sure you want to delete this data?")
//                setPositiveButton
                builder.setNegativeButton("Yes") { dialog, which ->
                    val position = adapterPosition
                    val addressId = addresslist[position].id
                    val serv: AddressAPI = Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(AddressAPI::class.java)
                    serv.deleteAddress(addressId).enqueue(object : retrofit2.Callback<Address> {
                        override fun onResponse(
                            call: retrofit2.Call<Address>,
                            response: retrofit2.Response<Address>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "Seccessfully Delete",
                                    Toast.LENGTH_LONG
                                ).show()
                                addresslist.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<Address>, t: Throwable) {
                            Toast.makeText(
                                context, "Update Failure",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
                builder.setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
            binding.edititem.setOnClickListener {
                val item = addresslist[adapterPosition]
                val contextView : Context = view.context
                val intant = Intent(contextView, AddressEdit::class.java)
                intant.putExtra("mId",item.id.toString())
                intant.putExtra("mAddress",item.address)
                intant.putExtra("mProvince",item.province)
                intant.putExtra("mDistrict",item.district)
                intant.putExtra("mZip_code",item.zip_code)
                intant.putExtra("mPhone",item.phone.toString())
                contextView.startActivity(intant)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = SessionManager(context)
        val name: String? = session.pref.getString(SessionManager.KEY_NAME, null)
        val binding = holder.binding
        val currentItem = addresslist!![position]

        binding.Namenaja.text = name
        binding.Address.text = "Address: " + currentItem.address
        binding.province.text = "Province: " + currentItem.province
        binding.district.text = "District: " + currentItem.district
        binding.textzipCode.text = "ZipCode: " + currentItem.zip_code
        binding.phone.text = "Phone: " + currentItem.phone

    }

    override fun getItemCount(): Int {
        return addresslist?.size ?: 0
    }
}
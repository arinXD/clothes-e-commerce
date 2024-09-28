package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.unicode.databinding.ActivityAdminShowEditDeleteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminShowEditDelete : AppCompatActivity() {
    private lateinit var bindingShow : ActivityAdminShowEditDeleteBinding
    var productList = arrayListOf<AdminProduct>()
    val createClient = AdminProductAPI.create()
    var mId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingShow = ActivityAdminShowEditDeleteBinding.inflate(layoutInflater)
        setContentView(bindingShow.root)
        mId = intent.getIntExtra("id",0)
        val mName = intent.getStringExtra("product_name")
        val mPrice = intent.getIntExtra("price",0)

        val mDetail = intent.getStringExtra("detail")
        val mPhoto = intent.getStringExtra("photo")
        val mAmount = intent.getIntExtra("amount",0)
        val mSubtype_id = intent.getIntExtra("product_type",0)

//        bindingShow.txtid.setText(mId.toString())
        bindingShow.txttitle.setText(mName.toString())
        bindingShow.txtprice.setText("฿" + mPrice.toString())
//        bindingShow.txtamout.setText("Amount: " + mAmount.toString())
//        bindingShow.txtimageProduct.setImageResource(mPhoto.toString().toInt())
        Glide.with(applicationContext).load(mPhoto.toString()).into(bindingShow.txtimageProduct)


        bindingShow.btnEdit.setOnClickListener {
            val intent = Intent(applicationContext, AdminEditProduct::class.java)
            println("___________\nfrom put intent")
            println(mId.toString())
            intent.putExtra("id",mId.toString())
            intent.putExtra("product_name",mName)
            intent.putExtra("price",mPrice)
            intent.putExtra("detail",mDetail)
            intent.putExtra("photo",mPhoto)
            intent.putExtra("amount",mAmount)
            intent.putExtra("product_type",mSubtype_id.toString())
            startActivity(intent)
        }
        bindingShow.btnDelete.setOnClickListener {
            daleteProduct()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home)
        { finish() }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        callProduct()
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
    fun callProduct() {
        productList.clear()
        createClient.retrieveProduct()
            .enqueue(object : Callback<List<AdminProduct>> {
                override fun onResponse(
                    call: Call<List<AdminProduct>>,
                    response: Response<List<AdminProduct>>
                ) {
                    response.body()?.forEach {
                        productList.add(AdminProduct(it.id,it.product_name,it.price,it.detail,it.photo,it.amount,it.product_type))
                    }
//                    bindingShow.recyclerViewInterface.id = EditProductsAdapter(productList, applicationContext)

//                    bindingShow.txttitle.text = "Name: ${productList!![this@ShowEditDeleteActivity].product_name}"
                }

                override fun onFailure(call: Call<List<AdminProduct>>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(applicationContext,"Error2", Toast.LENGTH_LONG).show()
                }
            })
    }
    fun daleteProduct() {
        val myBuilder = AlertDialog.Builder(this)
        myBuilder.apply {
            setTitle("DELETE")
            setMessage("Do you want to delete the Product?")
            setNegativeButton("Yes") { dialog, which ->
                createClient.daleteProduct(mId)
                    .enqueue(object : Callback<AdminProduct> {
                        override fun onResponse(call: Call<AdminProduct>, response: Response<AdminProduct>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    applicationContext, "Successfully Updated",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    applicationContext, "Delete Failure",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<AdminProduct>, t: Throwable) {
                            Toast.makeText(
                                applicationContext, "Error onFailure " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                finish()
            }
            setPositiveButton("No") { dialog, which -> dialog.cancel() }
            show()
        }
    }
}
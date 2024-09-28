package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.unicode.databinding.ActivityUserEditProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserEditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUserEditProfileBinding
    lateinit var session: SessionManager
    var uId=0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserEditProfileBinding.inflate(layoutInflater)
        session = SessionManager(applicationContext)
        uId = session.pref.getString(SessionManager.KEY_ID, null).toString().toInt()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSave.setOnClickListener {
            var email = binding.edtEmail.text.toString()
            var username = binding.edtUsername.text.toString()
            var api = UserAPI.create()
            if (username.isEmpty()){
                Toast.makeText(applicationContext, "insert username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            api.updateUser(uId, email, username)
                .enqueue(object : Callback<FindUserClass> {
                    override fun onResponse(call: Call<FindUserClass>, response: Response<FindUserClass>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "update success", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            println("cant update")
                        }
                    }

                    override fun onFailure(call: Call<FindUserClass>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(applicationContext,"d", Toast.LENGTH_LONG).show()
                    }

                })

        }

    }

    override fun onResume() {
        super.onResume()
        findUser()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun findUser(){
        var api = UserAPI.create()
        api.findUser(uId)
            .enqueue(object : Callback<FindUserClass> {
                override fun onResponse(call: Call<FindUserClass>, response: Response<FindUserClass>) {
                    if (response.isSuccessful) {
                        var email = response.body()?.email.toString()
                        var username = response.body()?.user_name.toString()
                        binding.edtEmail.setText(email)
                        binding.edtEmail.isEnabled = false
                        binding.edtUsername.setText(username)
                    } else {
                        println("cant find")
                    }
                }

                override fun onFailure(call: Call<FindUserClass>, t: Throwable) {
                    Toast.makeText(applicationContext,"Duplicate email", Toast.LENGTH_LONG).show()
                }

            })
    }
}
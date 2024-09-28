package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.unicode.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        session = SessionManager(applicationContext)
        if (session.isLoggedIn()) {
            if(session.getType()=="user"){
                var i: Intent = Intent(applicationContext, AllProducts::class.java)
                startActivity(i)
            }else{
                var i: Intent = Intent(applicationContext, AdminPage::class.java)
                startActivity(i)
            }
            finish()
        }
        if (!session.pref.getString(SessionManager.KEY_EMAIL, null).isNullOrEmpty()) {
            val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
            binding.userEmail.setText(email)
        }

//        var userEmail = binding.userEmail.text.toString()
//        var password = binding.userPassword.text.toString()

        binding.login.setOnClickListener {
            if (binding.userEmail.text.toString().isEmpty() || binding.userPassword.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Enter username and password.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                login()
            }
        }

        binding.register.setOnClickListener {
            val intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val api = UserAPI.create()
        api.login(
            binding.userEmail.text.toString(),
            binding.userPassword.text.toString()
        ).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val status = response.body()?.status.toString().toInt()
                if (status == 0) {
                    Toast.makeText(
                        applicationContext,
                        "The username or password is incorrect",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else {
                    try{
                        val id = response.body()?.user_id.toString()
                        val type = response.body()?.user_type.toString()
                        val username = response.body()?.user_name.toString()
                        val email = response.body()?.email.toString()
                        val gender = response.body()?.gender.toString()
                        val bDay = response.body()?.birthday.toString()
                        session.createLoginSession(
                            id,
                            type,
                            username,
                            email,
                            gender,
                            bDay
                        )
                        var i: Intent
                        if(type=="user"){
//                            Toast.makeText(applicationContext, type, Toast.LENGTH_LONG).show()
                            i = Intent(applicationContext, AllProducts::class.java)
                            startActivity(i)
                        }else{
                            Toast.makeText(applicationContext, "Hi $type", Toast.LENGTH_LONG).show()
                            i = Intent(applicationContext, AdminPage::class.java)
                            startActivity(i)
                        }
                        finish()
                    }catch (e:Exception){
                        Toast.makeText(applicationContext, "!!!!!!!!!!",Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Error onFailure " +
                            t.message, Toast.LENGTH_LONG
                ).show()
            }

        })

    }
}
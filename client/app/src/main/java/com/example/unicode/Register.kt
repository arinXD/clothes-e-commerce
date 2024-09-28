package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import com.example.unicode.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.male.isChecked = true

        var selectID: Int = binding.radioGroup.checkedRadioButtonId
        var radioButtonChecked: RadioButton = findViewById(selectID)
        var gender = radioButtonChecked.text

        binding.birthdayTxtDatePicker.setOnClickListener {
            val datePicker = DatePicker()
            datePicker.show(supportFragmentManager, "Date Picker")
        }

//        @Field("user_name") user_name:String,
//        @Field("email") email: String,
//        @Field("user_password") user_password:Int,
//        @Field("gender") gender: String,
//        @Field("birthday") birthday: String

        binding.submit.setOnClickListener{
            if(
                binding.edtUsername.text.toString().isEmpty() ||
                binding.edtEmail.text.toString().isEmpty() ||
                binding.edtPassword.text.toString().isEmpty() ||
                binding.confirmPassword.text.toString().isEmpty() ||
                binding.birthdayTxtDatePicker.text.toString() == "Select  birthday"
            ){
                Toast.makeText(applicationContext, "Please insert data", Toast.LENGTH_LONG).show()
            }
            else if(binding.edtPassword.text.toString()!=binding.confirmPassword.text.toString()){
                Toast.makeText(applicationContext, "Inconsistent password", Toast.LENGTH_LONG).show()
            }else{
                adduser(gender.toString())
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun adduser(gender: String){

        val api = UserAPI.create()
        api.insertUser(
            binding.edtUsername.text.toString(),
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString().toInt(),
            gender.toString(),
            binding.birthdayTxtDatePicker.text.toString()
        ).enqueue(object :Callback<AddUser>{
            override fun onResponse(call: Call<AddUser>, response: Response<AddUser>) {
                if (response.isSuccessful) {
                    val edit = SessionManager(applicationContext).edior
                    edit.clear()
                    edit.commit()
                    edit.putString(SessionManager.KEY_EMAIL, binding.edtEmail.text.toString())
                    edit.commit()
                    Toast.makeText(applicationContext,"Register Successfully",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext,"Inserted Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddUser>, t: Throwable) {
                Toast.makeText(applicationContext,"Duplicate email",Toast.LENGTH_LONG).show()
            }

        })

    }
}
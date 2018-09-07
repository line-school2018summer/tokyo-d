package com.example.toshiki.pusherchat

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_register.*
import android.content.Intent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body
import java.util.*
import com.google.gson.JsonObject


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Register"

        btnRegister.setOnClickListener {
            if (email.text.isNotEmpty() and register_username.text.isNotEmpty() and password.text.isNotEmpty() and password2.text.isNotEmpty()) {
                val email = email.text.toString()
                val user = register_username.text.toString()
                val password = password.text.toString()
                val password2 = password2.text.toString()

                if (password != password2) {
                    Toast.makeText(applicationContext,"Password is not same", Toast.LENGTH_SHORT).show()
                }

                val userRegister = User(
                        user,
                        password,
                        email
                )


                val call = UserRegister.create().postUserRegister(userRegister)

                call.enqueue(object : Callback<Identification> {
                    override fun onResponse(call: Call<Identification>, response: Response<Identification>) {
                        if (!response.isSuccessful) {
                            Toast.makeText(applicationContext,"Response was not successful", Toast.LENGTH_SHORT).show()
                        } else {
                            val res = response.body()

                            Log.d("Registration", res.toString())
                            Log.d("Registration", res!!.component1())
                            Log.d("Registration", res!!.component2())
                            Log.d("Registration", res!!.component3())
                            Log.d("Registration", res!!.component4())
                            Log.d("Registration", res!!.component5())

                            startActivity(Intent( this@RegisterActivity, HomeActivity::class.java))
                        }
                    }

                    override fun onFailure(call: Call<Identification>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error when calling the service", Toast.LENGTH_SHORT).show()
                    }
                })


            } else {
                Toast.makeText(applicationContext,"Username should not be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}

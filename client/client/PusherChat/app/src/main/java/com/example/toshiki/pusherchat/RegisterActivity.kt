package com.example.toshiki.pusherchat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_register.*
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body

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



                App.user = user
                startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))

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

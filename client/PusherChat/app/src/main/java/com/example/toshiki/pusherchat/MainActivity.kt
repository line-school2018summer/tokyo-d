package com.example.toshiki.pusherchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            if (username.text.isNotEmpty()) {
                val user = username.text.toString()

                App.user = user
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            } else {
                Toast.makeText(applicationContext,"Username should not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        btnToRegister.setOnClickListener {
            startActivity(Intent( this@MainActivity, RegisterActivity::class.java))
        }
    }
}

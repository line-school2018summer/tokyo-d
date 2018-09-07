package com.example.toshiki.pusherchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            if (login_email.text.isNotEmpty() and login_password.text.isNotEmpty()) {
                val email = login_email.text.toString()
                val password = login_password.text.toString()


                val call = SearchUserConfirm.create().postSearchUserConfirm(email)

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

                            App.user = res!!.component3()

                            startActivity(Intent( this@MainActivity, HomeActivity::class.java))
                        }
                    }

                    override fun onFailure(call: Call<Identification>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error when calling the service", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                Toast.makeText(applicationContext,"Username and Password should not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        btnToRegister.setOnClickListener {
            startActivity(Intent( this@MainActivity, RegisterActivity::class.java))
        }
    }
}

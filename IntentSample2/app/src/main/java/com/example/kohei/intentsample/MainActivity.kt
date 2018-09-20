package com.example.kohei.intentsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button = findViewById<Button>(R.id.returnButton)

        button.setOnClickListener {
            val intent = Intent(this, TirdActivity::class.java)
            startActivity(intent)
        }
    }
}
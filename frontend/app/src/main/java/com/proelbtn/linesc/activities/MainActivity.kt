package com.proelbtn.linesc.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.proelbtn.linesc.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.main)
    }
}

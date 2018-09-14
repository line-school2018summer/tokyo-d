package com.proelbtn.linesc.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.proelbtn.linesc.R

class LoginActivity : AppCompatActivity(), LoginPresenter.View {
    val presenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.login)

        findViewById<Button>(R.id.button_login).setOnClickListener {
            presenter.login()
        }
    }

    override fun getUserName(): String {
        return findViewById<TextView>(R.id.input_user).text.toString()
    }

    override fun getPassword(): String {
        return findViewById<TextView>(R.id.input_pass).text.toString()
    }

    override fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

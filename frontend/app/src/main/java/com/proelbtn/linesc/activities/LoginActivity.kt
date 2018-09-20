package com.proelbtn.linesc.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.proelbtn.linesc.R
import com.proelbtn.linesc.presenters.LoginPresenter

class LoginActivity : AppCompatActivity(), LoginPresenter.View {
    val presenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.button_login).setOnClickListener {
            presenter.onLogin()
        }
    }

    override fun getId(): String {
        return findViewById<EditText>(R.id.text_id).text.toString()
    }

    override fun getPassword(): String {
        return findViewById<EditText>(R.id.text_password).text.toString()
    }

    override fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

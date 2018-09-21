package com.proelbtn.linesc.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.proelbtn.linesc.R
import java.util.*

class EntryActivity : AppCompatActivity(), EntryPresenter.View {
    val presenter = EntryPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_entry)

        findViewById<Button>(R.id.button_login).setOnClickListener {
            presenter.onLogin()
        }

        findViewById<Button>(R.id.button_signup).setOnClickListener {
            presenter.onSignup()
        }

        presenter.onCreate()
    }

    override fun getContext(): Context {
        return this
    }

    override fun showMessage(msg: CharSequence, mode: Int) {
        Toast.makeText(this, msg, mode).show()
    }

    override fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

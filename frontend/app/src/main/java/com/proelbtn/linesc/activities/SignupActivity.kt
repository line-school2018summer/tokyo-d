package com.proelbtn.linesc.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.proelbtn.linesc.R
import com.proelbtn.linesc.presenters.SignupPresenter

class SignupActivity : AppCompatActivity(), SignupPresenter.View {
    val presenter = SignupPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_signup)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sign up"

        findViewById<Button>(R.id.button_signup).setOnClickListener {
            presenter.onSignup()
        }
    }

    override fun getContext(): Context {
        return this
    }

    override fun getId(): String {
        return findViewById<EditText>(R.id.text_id).text.toString()
    }

    override fun getName(): String {
        return findViewById<EditText>(R.id.text_name).text.toString()
    }

    override fun getPassword(): String {
        return findViewById<EditText>(R.id.text_password).text.toString()
    }

    override fun navigateToEntryActivity() {
        val intent = Intent(this, EntryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}

package com.proelbtn.linesc.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.proelbtn.linesc.R
import com.proelbtn.linesc.fragments.DashboardFragment
import com.proelbtn.linesc.fragments.HomeFragment
import com.proelbtn.linesc.presenters.MainPresenter

class MainActivity : AppCompatActivity(), MainPresenter.View {
    val home = HomeFragment()
    val dashboard = DashboardFragment()

    val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, home)
        transaction.add(R.id.container, dashboard)
        transaction.commit()

        findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.navigation_home -> presenter.selectHome()
                it.itemId == R.id.navigation_dashboard -> presenter.selectDashboard()
            }
            true
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun showHome() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, home)
        transaction.commit()
    }

    override fun showDashboard() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, dashboard)
        transaction.commit()
    }

    override fun navigateToChatActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

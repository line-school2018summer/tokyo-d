package com.proelbtn.linesc.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.proelbtn.linesc.R
import com.proelbtn.linesc.fragments.DashboardFragment
import com.proelbtn.linesc.fragments.HomeFragment
import com.proelbtn.linesc.fragments.NotificationsFragment
import com.proelbtn.linesc.presenters.MainPresenter

class MainActivity : AppCompatActivity(), MainPresenter.View {
    val home = HomeFragment()
    val dashboard = DashboardFragment()
    val notifications = NotificationsFragment()

    val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, home)
        transaction.add(R.id.container, dashboard)
        transaction.add(R.id.container, notifications)
        transaction.commit()

        findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.navigation_home -> presenter.selectHome()
                it.itemId == R.id.navigation_dashboard -> presenter.selectDashboard()
                it.itemId == R.id.navigation_notifications -> presenter.selectNotifications()
            }
            true
        }
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

    override fun showNotifications() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, notifications)
        transaction.commit()
    }

}

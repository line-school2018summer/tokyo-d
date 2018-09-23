package com.proelbtn.linesc.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.proelbtn.linesc.R
import com.proelbtn.linesc.fragments.DashboardFragment
import com.proelbtn.linesc.fragments.HomeFragment
import com.proelbtn.linesc.models.containers.HomeAdapterDataContainer
import com.proelbtn.linesc.presenters.MainPresenter

class MainActivity : AppCompatActivity(),
        MainPresenter.View,
        HomeFragment.Listener,
        DashboardFragment.Listener {
    val presenter = MainPresenter(this)

    val home = HomeFragment()
    val dashboard = DashboardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, home)
        transaction.commit()

        findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener {
            when {
                it.itemId == R.id.navigation_home -> {
                    presenter.onSelectHome()
                    true
                }
                it.itemId == R.id.navigation_dashboard -> {
                    presenter.onSelectDashboard()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreateFragmentView(fragment: Fragment) {
        if (fragment is HomeFragment) presenter.onCreateHomeFragmentView()
        if (fragment is DashboardFragment) presenter.onCreateDashboardFragmentView()
    }

    override fun onStartFragment(fragment: Fragment) {
        if (fragment is HomeFragment) presenter.onStartHomeFragment()
    }

    override fun onItemClicked(uid: String) {
        presenter.onItemClicked(uid)
    }

    override fun attachDataToHomeFragment(data: HomeAdapterDataContainer) {
        home.attachData(data)
        home.update()
    }

    override fun updateHomeFragment() {
        home.update()
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

    override fun navigateToChatActivity(uid: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("uid" , uid)
        startActivity(intent)
    }
}

package com.proelbtn.linesc.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.proelbtn.linesc.R
import com.proelbtn.linesc.fragments.DashboardFragment
import com.proelbtn.linesc.fragments.HomeFragment
import com.proelbtn.linesc.fragments.NotificationsFragment
import com.proelbtn.linesc.model.dataclass.PostUsers
import com.proelbtn.linesc.model.dataclass.ResPostUsers
import com.proelbtn.linesc.model.datainterface.UsersPost
import com.proelbtn.linesc.presenters.MainPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback

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

        UsersPost.create()
                .postUsers(PostUsers("aaa", "bbb", "ccc"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { it -> Log.d("test", it.toString())},
                        { it -> Log.d("error", it.toString())}
                )
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

    override fun navigateToChatActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

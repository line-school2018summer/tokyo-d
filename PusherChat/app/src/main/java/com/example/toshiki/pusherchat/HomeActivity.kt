package com.example.toshiki.pusherchat

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.toshiki.pusherchat.R.id.homeRecyclerView
import com.example.toshiki.pusherchat.R.menu.navigation


class HomeActivity : AppCompatActivity(), RecyclerViewHolder.ItemClickListener {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val friends = resources.getStringArray(R.array.friends).toMutableList()

                // Kotlin Android Extensionsを使っているので、つけたIDで直接指定できる（R.id.mainRecyclerView）
                homeRecyclerView.adapter = RecyclerAdapter(this, this, friends)
                homeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                val friends = resources.getStringArray(R.array.friends).toMutableList()

                // Kotlin Android Extensionsを使っているので、つけたIDで直接指定できる（R.id.mainRecyclerView）
                homeRecyclerView.adapter = RecyclerAdapter(this, this, friends)
                homeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {


                return@OnNavigationItemSelectedListener false
            }
        }
        false
    }

    override fun onItemClick(view: View, position: Int) {
        startActivity(Intent(this@HomeActivity, ChatActivity::class.java))
        Toast.makeText(applicationContext, "position $position was tapped", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}

package com.cubeofcheese.mastorithm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.cubeofcheese.mastorithm.Fragments.Chrono
import com.cubeofcheese.mastorithm.Fragments.Local
import com.cubeofcheese.mastorithm.Fragments.ScrollableFeed
import com.cubeofcheese.mastorithm.Fragments.Trending
import com.cubeofcheese.mastorithm.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("strings", Context.MODE_PRIVATE)
        var authToken = sharedPref?.getString("authtoken", "")

        if (authToken == "") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {


            tabLayout = findViewById(R.id.tabLayout)
            viewPager = findViewById(R.id.viewPager)

            val pageAdapter = PageAdapter(this, supportFragmentManager)
            pageAdapter.addPage(Chrono(), "Chrono")
            pageAdapter.addPage(Trending(), "Trending")
            pageAdapter.addPage(Local(), "Local")


            viewPager.adapter = pageAdapter
            tabLayout.setupWithViewPager(viewPager)

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val position = tab!!.position
                    (pageAdapter.getItem(position) as ScrollableFeed).scrollToTop()
                }
            })

        }
    }
}

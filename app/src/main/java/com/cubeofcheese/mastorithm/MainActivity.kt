package com.cubeofcheese.mastorithm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.cubeofcheese.mastorithm.Fragments.Chrono
import com.cubeofcheese.mastorithm.Fragments.Local
import com.cubeofcheese.mastorithm.Fragments.ScrollableFeed
import com.cubeofcheese.mastorithm.Fragments.Trending
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

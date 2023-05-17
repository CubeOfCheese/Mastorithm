package com.cubeofcheese.mastorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.cubeofcheese.mastorithm.Fragments.Home
import com.cubeofcheese.mastorithm.Fragments.Local
import com.cubeofcheese.mastorithm.Fragments.Trending
import com.cubeofcheese.mastorithm.R.id.feed
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

        pageAdapter.addPage(Home(), "Home")
        pageAdapter.addPage(Trending(), "Trending")
        pageAdapter.addPage(Local(), "Local")


        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}

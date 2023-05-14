package com.cubeofcheese.mastorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.cubeofcheese.mastorithm.Fragments.Home
import com.cubeofcheese.mastorithm.Fragments.Local
import com.cubeofcheese.mastorithm.Fragments.Trending
import com.cubeofcheese.mastorithm.models.PostModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.keylesspalace.tusky.util.parseAsMastodonHtml
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        val pageAdapter = PageAdapter(this, supportFragmentManager)

        pageAdapter.addPage(Home(), "Home.kt")
        pageAdapter.addPage(Trending(), "Trending")
        pageAdapter.addPage(Local(), "Local")


        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}

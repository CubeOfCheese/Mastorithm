package com.cubeofcheese.mastorithm.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cubeofcheese.mastorithm.ApiInterface
import com.cubeofcheese.mastorithm.PostAdapter
import com.cubeofcheese.mastorithm.R
import com.cubeofcheese.mastorithm.models.TestData
import com.cubeofcheese.mastorithm.models.PostModel
import com.cubeofcheese.mastorithm.util.generatePost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Trending : Fragment(), ScrollableFeed {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var feed: ArrayList<PostModel>
    lateinit var swipeToRefresh : SwipeRefreshLayout
    lateinit var adapter: PostAdapter
    lateinit var authtoken : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPref = this.activity?.getSharedPreferences("strings", Context.MODE_PRIVATE)
        authtoken = sharedPref?.getString("authtoken", "").toString()

        feed = arrayListOf<PostModel>()
        refreshFeed()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newRecyclerView = view.findViewById(R.id.feed)
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)

        setupRefreshBehavior(view)

        newRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    fetchFeed(feed.size)
                }
            }
        })
    }

    private fun setupRefreshBehavior(view: View) {
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)

        swipeToRefresh.setOnRefreshListener {
            refreshFeed()
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun refreshFeed() {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getTrendingStatuses("Bearer $authtoken", 0)

        retrofitData.enqueue(object: Callback<List<TestData>?> {
            override fun onResponse (call: Call<List<TestData>?>, response: Response<List<TestData>?>) {
                val responseBody = response.body()!!
                feed = arrayListOf<PostModel>()

                for (status in responseBody) {
                    var post: PostModel = generatePost(status)

                    feed.add(post)
                }
                adapter = activity?.let { PostAdapter(feed, it) }!!
                newRecyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<TestData>?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })
    }

    private fun fetchFeed(offset: Int) {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getTrendingStatuses("Bearer $authtoken", offset)

        retrofitData.enqueue(object: Callback<List<TestData>?> {
            override fun onResponse (call: Call<List<TestData>?>, response: Response<List<TestData>?>) {
                val responseBody = response.body()!!

                for (status in responseBody) {
                    var post: PostModel = generatePost(status)

                    feed.add(post)
                    adapter.notifyItemInserted(offset);
                }
            }

            override fun onFailure(call: Call<List<TestData>?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })
    }
    override fun scrollToTop() {
        newRecyclerView.scrollToPosition(0)
    }
}
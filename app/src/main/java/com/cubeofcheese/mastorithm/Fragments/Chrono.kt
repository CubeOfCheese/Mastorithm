package com.cubeofcheese.mastorithm.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cubeofcheese.mastorithm.*
import com.cubeofcheese.mastorithm.models.PostModel
import com.cubeofcheese.mastorithm.util.generatePost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://mstdn.social"

class Chrono : Fragment(), ScrollableFeed {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var feed: ArrayList<PostModel>
    lateinit var swipeToRefresh : SwipeRefreshLayout
    lateinit var repliesCount : Button
    lateinit var adapter: PostAdapter
    var lock = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        feed = arrayListOf<PostModel>()
        refreshFeed(null)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chrono, container, false)
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
                if (!lock && !recyclerView.canScrollVertically(1)) {
                    fetchFeed(feed.size)
                }
            }
        })

//        repliesCount = view.findViewById(R.id.repliesCount)
//
//
//        repliesCount.setOnClickListener {
//            activity?.let {
//                val intent = Intent(it, ThreadActivity::class.java)
//                intent.putExtra("statusId", "110533224291142854")
//                it.startActivity(intent)
//            }
//        }
    }

    private fun setupRefreshBehavior(view: View) {
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)

        swipeToRefresh.setOnRefreshListener {

            //  TODO: this relies on the reblog status id, not the original status id. Need to fix
            refreshFeed(feed[0].id)
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun refreshFeed(sinceId: String?) {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData(sinceId, null)

        retrofitData.enqueue(object: Callback<List<TestData>?> {
            override fun onResponse (call: Call<List<TestData>?>, response: Response<List<TestData>?>) {
                val responseBody = response.body()!!
                val refreshArrayList = arrayListOf<PostModel>()

                for (status in responseBody) {
                    var post: PostModel = generatePost(status)

                    refreshArrayList.add(post)
                }
                feed.addAll(0, refreshArrayList)

                adapter = activity?.let { PostAdapter(feed, it) }!!
                newRecyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<TestData>?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })
    }

    private fun fetchFeed(offset: Int) {
        lock = true
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData(null, feed[feed.size-1].id)

        retrofitData.enqueue(object: Callback<List<TestData>?> {
            override fun onResponse (call: Call<List<TestData>?>, response: Response<List<TestData>?>) {
                val responseBody = response.body()!!

                for (status in responseBody) {
                    var post: PostModel = generatePost(status)

                    feed.add(post)
                }
                adapter.notifyDataSetChanged()
                lock = false
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
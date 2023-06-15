package com.cubeofcheese.mastorithm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cubeofcheese.mastorithm.models.PostModel
import com.cubeofcheese.mastorithm.models.StatusContext
import com.cubeofcheese.mastorithm.util.generatePost
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ThreadActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var ancestors: ArrayList<PostModel>
    private lateinit var descendants: ArrayList<PostModel>
    lateinit var swipeToRefresh : SwipeRefreshLayout
    lateinit var adapter: PostAdapter
    lateinit var authtoken : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)

        val gson = Gson()
        val status = gson.fromJson(intent.getStringExtra("status"), PostModel::class.java)
        if (status != null) {
            getContext(status)
        }
        ancestors = arrayListOf<PostModel>()
        descendants = arrayListOf<PostModel>()


        // Inflate the layout for this fragment
//            return inflater.inflate(R.layout.activity_thread, container, false)

        newRecyclerView = findViewById(R.id.feed)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

//        setupRefreshBehavior(view)


//        newRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!lock && !recyclerView.canScrollVertically(1)) {
//                    fetchFeed(feed.size)
//                }
//            }
//        })
    }



    private fun getContext(status: PostModel) {
        val sharedPref = getSharedPreferences("strings", Context.MODE_PRIVATE)
        var server = sharedPref?.getString("server", "")
        var authtoken = sharedPref?.getString("authtoken", "")

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(server)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getStatusContext("Bearer $authtoken", status.id)

        retrofitData.enqueue(object: Callback<StatusContext?> {
            override fun onResponse (call: Call<StatusContext?>, response: Response<StatusContext?>) {
                val responseBody = response.body()!!
                val ancestorArrayList = arrayListOf<PostModel>()
                val descendantArrayList = arrayListOf<PostModel>()

                for (status in responseBody.ancestors) {
                    var post: PostModel = generatePost(status)

                    ancestorArrayList.add(post)
                }

                for (status in responseBody.descendants) {
                    var post: PostModel = generatePost(status)

                    descendantArrayList.add(post)
                }

                val thread = arrayListOf<PostModel>()
                thread.addAll(ancestorArrayList)
                thread.add(status)
                thread.addAll(descendantArrayList)

                adapter = PostAdapter(thread, this@ThreadActivity)
                newRecyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<StatusContext?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })
    }

    private fun setupRefreshBehavior(view: View) {
        swipeToRefresh = findViewById(R.id.swipeToRefresh)

        swipeToRefresh.setOnRefreshListener {
//            refreshFeed(feed[0].id)
//            TODO: implement refresh
            swipeToRefresh.isRefreshing = false
        }
    }
}


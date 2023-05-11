package com.cubeofcheese.mastorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.keylesspalace.tusky.util.parseAsMastodonHtml
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://mstdn.social"
class MainActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var feed: ArrayList<PostModel>
    lateinit var displayNames : Array<String>
    lateinit var usernames : Array<String>
    lateinit var postContents : Array<String>
    lateinit var swipeToRefresh : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newRecyclerView = findViewById(R.id.feed)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)


        feed = arrayListOf<PostModel>()
        getMyData()
        refreshApp()
    }

    private fun refreshApp() {
        swipeToRefresh = findViewById(R.id.swipeToRefresh)

        swipeToRefresh.setOnRefreshListener {
            getMyData()
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object: Callback<List<TestData>?> {
            override fun onResponse (call: Call<List<TestData>?>, response: Response<List<TestData>?>) {
                val responseBody = response.body()!!
                val refreshArrayList = arrayListOf<PostModel>()

                for (myData in responseBody) {
                    val post = PostModel(
                        myData.account.display_name,
                        myData.account.acct,
                        myData.account.avatar_static,
                        myData.content.parseAsMastodonHtml()
                    )
                    refreshArrayList.add(post)
                }
                feed.addAll(0, refreshArrayList)


                newRecyclerView.adapter = PostAdapter(feed)
            }

            override fun onFailure(call: Call<List<TestData>?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })
    }

}
package com.cubeofcheese.mastorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cubeofcheese.mastorithm.models.PostModel
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
    lateinit var swipeToRefresh : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newRecyclerView = findViewById(R.id.feed)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)


        feed = arrayListOf<PostModel>()
        initializeHomeFeed()
        setupRefreshBehavior()
    }

    private fun setupRefreshBehavior() {
        swipeToRefresh = findViewById(R.id.swipeToRefresh)

        swipeToRefresh.setOnRefreshListener {
            refreshHomeFeed(feed[0].id)
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun initializeHomeFeed() {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object: Callback<List<TestData>?> {
            override fun onResponse (call: Call<List<TestData>?>, response: Response<List<TestData>?>) {
                val responseBody = response.body()!!
                val refreshArrayList = arrayListOf<PostModel>()
                val seenAccountList = arrayListOf<String>()

                for (status in responseBody) {
                    var post: PostModel;
                    if (status.mediaAttachments != null && status.mediaAttachments.isNotEmpty() && status.mediaAttachments[0].type == "image") {
                        post = PostModel(
                            status.id,
                            status.account.display_name,
                            status.account.acct,
                            status.account.avatar_static,
                            status.content.parseAsMastodonHtml(),
                            status.mediaAttachments[0].preview_url
                        )
                    } else {
                        post = PostModel(
                            status.id,
                            status.account.display_name,
                            status.account.acct,
                            status.account.avatar_static,
                            status.content.parseAsMastodonHtml(),
                            null
                        )
                    }

                    if (!seenAccountList.contains(post.username)) {
                        refreshArrayList.add(post)
                        seenAccountList.add(post.username)
                    }
                }
                feed.addAll(0, refreshArrayList)

                newRecyclerView.adapter = PostAdapter(feed)
            }

            override fun onFailure(call: Call<List<TestData>?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })
    }

    private fun refreshHomeFeed(sinceId: String) {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData(sinceId)

        retrofitData.enqueue(object: Callback<List<TestData>?> {
            override fun onResponse (call: Call<List<TestData>?>, response: Response<List<TestData>?>) {
                val responseBody = response.body()!!
                val refreshArrayList = arrayListOf<PostModel>()

                for (status in responseBody) {
                    var post: PostModel;
                    if (status.mediaAttachments.isNotEmpty() && status.mediaAttachments[0].type == "image") {
                        post = PostModel(
                            status.id,
                            status.account.display_name,
                            status.account.acct,
                            status.account.avatar_static,
                            status.content.parseAsMastodonHtml(),
                            status.mediaAttachments[0].preview_url
                        )
                    } else {
                        post = PostModel(
                            status.id,
                            status.account.display_name,
                            status.account.acct,
                            status.account.avatar_static,
                            status.content.parseAsMastodonHtml(),
                            null
                        )
                    }

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
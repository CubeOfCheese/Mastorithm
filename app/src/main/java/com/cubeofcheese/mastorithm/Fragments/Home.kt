package com.cubeofcheese.mastorithm.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cubeofcheese.mastorithm.*
import com.cubeofcheese.mastorithm.models.PostModel
import com.keylesspalace.tusky.util.parseAsMastodonHtml
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://mstdn.social"

class Home : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var feed: ArrayList<PostModel>
    lateinit var swipeToRefresh : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        feed = arrayListOf<PostModel>()
        refreshHomeFeed(null)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newRecyclerView = view.findViewById(R.id.feed)
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)

        setupRefreshBehavior(view)
    }

    private fun setupRefreshBehavior(view: View) {
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)

        swipeToRefresh.setOnRefreshListener {
            refreshHomeFeed(feed[0].id)
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun refreshHomeFeed(sinceId: String?) {
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
                    if (status.reblog != null) {
                        if (status.reblog.mediaAttachments.isNotEmpty() && status.reblog.mediaAttachments[0].type == "image") {
                            post = PostModel(
                                status.reblog.id,
                                status.reblog.account.display_name,
                                status.reblog.account.acct,
                                status.reblog.account.avatar_static,
                                status.reblog.content.parseAsMastodonHtml(),
                                status.reblog.mediaAttachments[0].preview_url,
                                "Boosted by " + status.account.display_name
                            )
                        } else {
                            post = PostModel(
                                status.reblog.id,
                                status.reblog.account.display_name,
                                status.reblog.account.acct,
                                status.reblog.account.avatar_static,
                                status.reblog.content.parseAsMastodonHtml(),
                                null,
                                "Boosted by " + status.account.display_name
                            )
                        }
                    }
                    else {
                        if (status.mediaAttachments.isNotEmpty() && status.mediaAttachments[0].type == "image") {
                            post = PostModel(
                                status.id,
                                status.account.display_name,
                                status.account.acct,
                                status.account.avatar_static,
                                status.content.parseAsMastodonHtml(),
                                status.mediaAttachments[0].preview_url,
                                null
                            )
                        } else {
                            post = PostModel(
                                status.id,
                                status.account.display_name,
                                status.account.acct,
                                status.account.avatar_static,
                                status.content.parseAsMastodonHtml(),
                                null,
                                null
                            )
                        }
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
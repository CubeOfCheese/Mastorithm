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
    private lateinit var newArrayList: ArrayList<PostModel>
    lateinit var displayNames : Array<String>
    lateinit var usernames : Array<String>
    lateinit var postContents : Array<String>
    lateinit var swipeToRefresh : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayNames = arrayOf(
            "David",
            "Alex",
            "Maria",
            "Marco"
        )
        usernames = arrayOf(
            "@david@mastodon.social",
            "@alex@mstdn.social",
            "@maria@mstdn.jp",
            "@marco@mastodon.cloud"
        )
        postContents = arrayOf(
            "asperiores corrupti est fugit veritatis vel accusantium est rerum sapiente necessitatibus aperiam laudantium cumque iure non sunt odio accusamus aliquid",
            "modi ipsum doloremque velit accusamus ullam occaecati quia deleniti officiis voluptatem nesciunt omnis sapiente laboriosam ipsa excepturi et quia asperiores ",
            " quam molestiae aut quo explicabo natus sit doloribus impedit sequi molestiae beatae itaque nobis dolorem error non quia deleniti ipsum ",
            "perspiciatis nihil repellat modi ab consequatur tempora eius in adipisci animi doloribus et quia excepturi eos sint temporibus voluptatem dolore "
        )

        newRecyclerView = findViewById(R.id.feed)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)


        newArrayList = arrayListOf<PostModel>()
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

                val myStringBuilder = StringBuilder()
                for (myData in responseBody) {
                    val post = PostModel(
                        myData.account.display_name,
                        myData.account.acct,
                        myData.account.avatar_static,
                        myData.content.parseAsMastodonHtml()
                    )
                    newArrayList.add(0, post)
                }

                newRecyclerView.adapter = PostAdapter(newArrayList)

            }

            override fun onFailure(call: Call<List<TestData>?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })
    }

}
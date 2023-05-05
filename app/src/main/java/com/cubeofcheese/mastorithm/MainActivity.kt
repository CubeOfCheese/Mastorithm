package com.cubeofcheese.mastorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<PostModel>
    lateinit var displayNames : Array<String>
    lateinit var usernames : Array<String>
    lateinit var postContents : Array<String>

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
        getUserData()

    }


    private fun getUserData() {
        for (i in postContents.indices) {
            val post = PostModel(displayNames[i], usernames[i], postContents[i])
            newArrayList.add(post)
        }

        newRecyclerView.adapter = PostAdapter(newArrayList)
    }

}
package com.cubeofcheese.mastorithm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cubeofcheese.mastorithm.models.PostModel
import com.cubeofcheese.mastorithm.models.TestData
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostAdapter(private val postList : ArrayList<PostModel>, var context: Context ) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    var mainActivity = context as AppCompatActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_post, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = postList[position]
        val gson = Gson()
        holder.presenceReason.text = currentItem.presenceReason
        holder.displayName.text = currentItem.displayName
        holder.username.text = currentItem.username
        holder.postContent.text = currentItem.postContent
        Picasso
            .get()
            .load(currentItem.imagePreview)
            .into(holder.imagePreview);
        Picasso
            .get()
            .load(currentItem.avatar)
            .placeholder(R.drawable.default_profile_picture)
            .into(holder.avatar);

        holder.repliesCount.text = currentItem.repliesCount.toString() + " Replies"
        holder.repliesCount.setOnClickListener {
            mainActivity?.let {
                val intent = Intent(it, ThreadActivity::class.java)
                intent.putExtra("status", gson.toJson(currentItem))
                mainActivity.startActivity(intent)
            }
        }
        holder.reblogsCount.text = currentItem.reblogsCount.toString() + " Boosts"
        holder.reblogsCount.setOnClickListener {
            boostStatus(context, currentItem.id)
        }
        holder.favouritesCount.text = currentItem.favoritesCount.toString() + " Stars"
        holder.favouritesCount.setOnClickListener {
            favouriteStatus(context, currentItem.id)
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val presenceReason : TextView = itemView.findViewById(R.id.presenceReason)
        val displayName : TextView = itemView.findViewById(R.id.displayName)
        val username : TextView = itemView.findViewById(R.id.username)
        val avatar : ImageView = itemView.findViewById(R.id.avatar)
        val postContent : TextView = itemView.findViewById(R.id.postContent)
        val imagePreview : ImageView = itemView.findViewById(R.id.imagePreview)
        val repliesCount : Button = itemView.findViewById(R.id.repliesCount)
        val reblogsCount : TextView = itemView.findViewById(R.id.reblogsCount)
        val favouritesCount : TextView = itemView.findViewById(R.id.favouritesCount)
    }

}

private fun boostStatus(context: Context, statusId: String) {
    val sharedPref = context?.getSharedPreferences("strings", Context.MODE_PRIVATE)
    var authtoken = sharedPref?.getString("authtoken", "").toString()
    var server = sharedPref?.getString("server", "").toString()

    val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl(server)
        .build()
        .create(ApiInterface::class.java)

    val retrofitData = retrofitBuilder.boostStatus("Bearer $authtoken", statusId)

    retrofitData.enqueue(object: Callback<TestData?> {
        override fun onResponse (call: Call<TestData?>, response: Response<TestData?>) {
            Log.d("MainAc", "onSuccess: boosted" + response.body()?.content)
        }

        override fun onFailure(call: Call<TestData?>, t: Throwable) {
            Log.d("MainAc", "onFailure: "+ t.message)
        }
    })
}

private fun favouriteStatus(context: Context, statusId: String) {
    val sharedPref = context?.getSharedPreferences("strings", Context.MODE_PRIVATE)
    var authtoken = sharedPref?.getString("authtoken", "").toString()
    var server = sharedPref?.getString("server", "").toString()

    val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl(server)
        .build()
        .create(ApiInterface::class.java)

    val retrofitData = retrofitBuilder.favouriteStatus("Bearer $authtoken", statusId)

    retrofitData.enqueue(object: Callback<TestData?> {
        override fun onResponse (call: Call<TestData?>, response: Response<TestData?>) {
            var responseBody = response.body()!!
            Log.d("MainAc", "onSuccess: Favourited"  + responseBody.content)
        }

        override fun onFailure(call: Call<TestData?>, t: Throwable) {
            Log.d("MainAc", "onFailure: "+ t.message)
        }
    })
}
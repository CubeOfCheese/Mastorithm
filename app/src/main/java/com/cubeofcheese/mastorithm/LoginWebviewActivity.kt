package com.cubeofcheese.mastorithm
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.cubeofcheese.mastorithm.Fragments.BASE_URL
import com.cubeofcheese.mastorithm.models.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginWebviewActivity : Activity() {
    private var webView: WebView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_webview)
        val serverUrl = intent.getStringExtra("serverUrl")
//        var clientId = intent.getStringExtra("clientId")
        val sharedPref = getSharedPreferences("strings", Context.MODE_PRIVATE)
        val clientId = sharedPref?.getString("clientId", "").toString()
        val clientSecret = sharedPref?.getString("clientSecret", "").toString()

        webView = findViewById<View>(R.id.webView) as WebView
        webView!!.settings.javaScriptEnabled = true

        webView!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // Here put your code
                Log.d("My Webview", url)

                if ("oauth2redirectrhythm://" in url) {
                    var code = url.substringAfter("oauth2redirectrhythm://me.com?code=")

                    val retrofitBuilder = Retrofit.Builder().addConverterFactory(
                        GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build()
                        .create(ApiInterface::class.java)

                    val retrofitData = retrofitBuilder.getOauthToken(clientId,
                            clientSecret,
                            "oauth2redirectRhythm://me.com",
                            "authorization_code",
                            code,
                            "read write push"
                    )

                    retrofitData.enqueue(object: Callback<Token?> {
                        override fun onResponse(
                            call: Call<Token?>,
                            response: Response<Token?>
                        ) {
                            val responseBody = response.body()!!
                            val sharedPref = getSharedPreferences("strings", Context.MODE_PRIVATE)
                            with (sharedPref.edit()) {
                                putString(getString(R.string.authtoken), responseBody.access_token)
                                apply()
                            }

                            this?.let {
                                val intent = Intent(this@LoginWebviewActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<Token?>, t: Throwable) {
                            Log.d("MainAc", "onFailure: " + t.message)
                        }
                    })



//                    val sharedPref = getSharedPreferences("strings", Context.MODE_PRIVATE)
//                    with (sharedPref.edit()) {
//                        putString(getString(R.string.authcode), code)
//                        apply()
//                    }

                    return true
                }

                // return true; //Indicates WebView to NOT load the url;
                return false //Allow WebView to load url
            }
        })

        val builder = Uri.Builder()
        builder.scheme("https")
            .authority(serverUrl)
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("scope", "read write push")
            .appendQueryParameter("redirect_uri", "oauth2redirectrhythm://me.com")
            .appendQueryParameter("response_type", "code")
        val myUrl = builder.build().toString()

        webView!!.loadUrl(myUrl)
    }
}
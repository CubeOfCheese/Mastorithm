package com.cubeofcheese.mastorithm.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.cubeofcheese.mastorithm.ApiInterface
import com.cubeofcheese.mastorithm.Fragments.BASE_URL
import com.cubeofcheese.mastorithm.LoginWebviewActivity
import com.cubeofcheese.mastorithm.databinding.ActivityLoginBinding

import com.cubeofcheese.mastorithm.R
import com.cubeofcheese.mastorithm.models.Application
import com.cubeofcheese.mastorithm.models.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var application: Application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val server = binding.server
        val login = binding.login
        val loading = binding.loading


        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val applicationData = retrofitBuilder.authenticateApp("Rhythm",
            "oauth2redirectrhythm://me.com",
            "read write push",
            "https://cubeofcheese.com")

        applicationData.enqueue(object: Callback<Application?> {
            override fun onResponse (call: Call<Application?>, response: Response<Application?>) {
                application = response.body()!!

                val sharedPref = getSharedPreferences("strings", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString(getString(R.string.clientId), application.client_id)
                    putString(getString(R.string.clientSecret), application.client_secret)
                    apply()
                }

                val retrofitData = retrofitBuilder.getOauthToken(application.client_id,
                    application.client_secret,
                    "oauth2redirectrhythm://me.com",
                    "client_credentials")

                retrofitData.enqueue(object: Callback<Token?> {
                    override fun onResponse (call: Call<Token?>, response: Response<Token?>) {
                        val responseBody = response.body()!!
                        // save token
                        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
                        with (sharedPref.edit()) {
                            putString(getString(R.string.oauth_token), responseBody.access_token)
                            apply()
                        }
                    }

                    override fun onFailure(call: Call<Token?>, t: Throwable) {
                        Log.d("MainAc", "onFailure: "+t.message)
                    }
                })
            }

            override fun onFailure(call: Call<Application?>, t: Throwable) {
                Log.d("MainAc", "onFailure: "+t.message)
            }
        })

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                if (server != null) {
                    server.error = getString(loginState.usernameError)
                }
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loading != null) {
                loading.visibility = View.GONE
            }
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        if (server != null) {
            server.afterTextChanged {
            }
        }

        login.setOnClickListener {

            if (server != null && server.text.toString() != "") {
                val sharedPref = getSharedPreferences("strings", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("server", "https://" + server.text.toString())
                    apply()
                }

                this?.let {
                    val intent = Intent(it, LoginWebviewActivity::class.java)
                    intent.putExtra("serverUrl", server.text.toString())
                    intent.putExtra("clientId", application.client_id)
                    startActivity(intent)
                }
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


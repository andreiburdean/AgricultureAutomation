package com.example.agricultureautomationapp.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agricultureautomationapp.R
import com.example.agricultureautomationapp.apiservices.LoginApiService
import com.example.agricultureautomationapp.apiservices.LoginRequest
import com.example.agricultureautomationapp.apiservices.LoginResponse
import com.example.agricultureautomationapp.environments.EnvironmentsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity()  {

    private val BASE_URL = "http://10.0.2.2:8080";

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerQuestion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.email_field)
        passwordInput = findViewById(R.id.password_field)
        loginButton = findViewById(R.id.login_button)
        registerQuestion = findViewById(R.id.register)
    }

    private fun setListeners() {
        loginButton.setOnClickListener {
            val validInputs = validateInputs()

            if (validInputs) {
                val enteredEmail = emailInput.text.toString()
                val enteredPassword = passwordInput.text.toString()

                performLogin(enteredEmail, enteredPassword)
            }
        }

        registerQuestion.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin(email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val loginApi = retrofit.create(LoginApiService::class.java)
        val request = LoginRequest(email, password)

        loginApi.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d(TAG, "Login successful, Token: ${loginResponse?.userId}")
                    startActivity(Intent(this@LoginActivity, EnvironmentsActivity::class.java))
                } else {
                    val loginResponse = response.body()
                    Log.d(TAG, "Login failed, Token: ${loginResponse?.userId}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("LoginActivity", "API call error: ${t.message}", t)
            }

        })
    }

    private fun validateInputs(): Boolean {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_login_fields), Toast.LENGTH_SHORT).show()
            return false
        }

        if (!email.contains("@")) {
            Toast.makeText(this, getString(R.string.invalid_email_format), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

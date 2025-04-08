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
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity()  {

    private val BASE_URL = "http://10.0.2.2:8080"
//private val BASE_URL = "http://192.168.100.63:8080"
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        emailInput = findViewById(R.id.email_field)
        passwordInput = findViewById(R.id.password_field)
        registerButton = findViewById(R.id.login_button)
    }

    private fun setListeners() {
        registerButton.setOnClickListener {
            val validInputs = validateInputs()

            if (validInputs) {
                val enteredEmail = emailInput.text.toString()
                val enteredPassword = passwordInput.text.toString()

                createAccount(enteredEmail, enteredPassword)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val registerUserApi = retrofit.create(LoginApiService::class.java)
        val request = LoginRequest(email, password)

        registerUserApi.registerUser(request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when (response.code()) {
                    201 -> {
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        Log.d(TAG, "User created: " + response.code())
                    }
                    409 -> {
                        Toast.makeText(this@RegisterActivity, "User already exists", Toast.LENGTH_SHORT).show()
                        emailInput.setText("");
                        passwordInput.setText("");
                    }
                    else -> {
                        Log.d(TAG, "User creation failure: " + response.code())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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

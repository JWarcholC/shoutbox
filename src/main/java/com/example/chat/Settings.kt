package com.example.chat

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var shared = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val login = shared.getString("LOGIN", "")
        val loginName = findViewById<TextView>(R.id.asdf)

        loginName.text = login

        val loginButton = findViewById<Button>(R.id.setLogin)
        loginButton.setOnClickListener {

            if (!isInternetConnection()) {
                Toast.makeText(this@Settings, "No internet connection!",
                        Toast.LENGTH_LONG)
                        .show()

            } else {
                val savedLogin = loginName.text

                saveData(savedLogin.toString())
                startMainActivity()
            }
        }
    }

    private fun saveData(data: String) {
        val shared = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putString("LOGIN", data)
        editor.apply()

    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @Suppress("DEPRECATION")
    private fun isInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }
}

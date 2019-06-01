package com.example.chat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.widget.Button
import android.widget.TextView

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        var shared = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val login = shared.getString("LOGIN", null)

    //   shared = getDefaultSharedPreferences(this@MainActivity)
      //  val isReturned = shared.getBoolean("RETURN_STATE", false)

        if(login != null) {
            startMainActivity()
        }

        val loginButton = findViewById<Button>(R.id.setLogin)
        loginButton.setOnClickListener {
            val loginName = findViewById<TextView>(R.id.login)
            val savedLogin = loginName.text

            saveData(savedLogin.toString())
            startMainActivity()
        }
    }

    private fun saveData(data : String) {
        val shared = getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val editor = shared.edit()
        editor.putString("LOGIN", data)
        editor.apply()

    }


    private fun startMainActivity() {
        val intent =  Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

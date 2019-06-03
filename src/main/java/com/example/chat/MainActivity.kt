package com.example.chat

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import com.example.chat.com.example.chat.JSON.JSONChatInterface
import com.example.chat.com.example.chat.JSON.Post
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    companion object {
        val toDeleteList = ArrayList<String>()
    }
    val  URL  = "http://tgryl.pl/shoutbox/"
    private val postsList = ArrayList<Post>()
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: MessageAdapter
    private lateinit var thread : Thread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        var messageAPI = getJSON()
        getMessages(messageAPI)

        val fab: FloatingActionButton = findViewById(R.id.send)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        mRecyclerView = findViewById(R.id.message)
        mRecyclerView.setHasFixedSize(true)

        //add margin for single message
        mRecyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL))
        mRecyclerView.addItemDecoration( DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        mLayoutManager = LinearLayoutManager(this)
        mAdapter = MessageAdapter(postsList, this)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter





        fab.setOnClickListener { view ->
            val msg = findViewById<EditText>(R.id.send_message)

            if(!msg.toString().isEmpty() || msg == null) {
                 var shared = getSharedPreferences("DATA", Context.MODE_PRIVATE)
                 val login = shared.getString("LOGIN", null)

                 if(login != null) {
                     val content = msg.text.toString()
                     sendMessage(login, content)
                     messageAPI = getJSON()

                     removePosts()
                     getMessages(messageAPI)
                     msg.setText("")
                 }
             } else {
                 AlertDialog.Builder(this)
                         .setTitle("Attention")
                         .setMessage("Enter message then send it")
                         .show()
             }
        }

        thread = Thread{
            while(!thread.isInterrupted) {
               // messageAPI = getJSON()

                //removePosts()
                //getMessages(messageAPI)
              //  mRecyclerView.adapter = mAdapter
                SystemClock.sleep(15 * 1000 /* ms */)
            }
        }
       // thread.run()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> false
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                goToSettings()
            }
            R.id.nav_shoutbox -> {

                if(!isInternetConnection()) {
                    Toast.makeText(this@MainActivity, "No internet connection!",
                            Toast.LENGTH_LONG)
                            .show()
                   return true
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_refresh -> {

                if(!isInternetConnection()) {
                    Toast.makeText(this@MainActivity, "No internet connection!",
                            Toast.LENGTH_LONG)
                            .show()
                    return true
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getMessages(messageAPI : JSONChatInterface) {
        val call = messageAPI.getPosts()

        doAsync {
            call.enqueue(object : Callback<List<Post>> {
                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    AlertDialog.Builder(this@MainActivity)
                            .setTitle("Exception")
                            .setMessage("Exception:" + t.message)
                            .show()

                    return
                }

                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {

                    if (!response.isSuccessful) {
                        AlertDialog.Builder(this@MainActivity)
                                .setTitle("Error")
                                .setMessage("Error code: " + response.code())
                                .show()

                        return
                    }

                    val posts = response.body()


                    if (posts != null) {
                        postsList.clear()
                        postsList.addAll(posts)
                        mRecyclerView.adapter = mAdapter
                    }
                }

            })
        }
    }

    private fun sendMessage(login : String, message: String) {

        if(!isInternetConnection()) {
            Toast.makeText(this@MainActivity, "No internet connection!",
                    Toast.LENGTH_LONG)
                    .show()
            return
        }

        val json = getJSON()

        val call = json.postPost(message, login)
        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Toast.makeText(this@MainActivity,
                        "post submitted to server." + response.body().toString(),
                        Toast.LENGTH_LONG)
                        .show()
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Error")
                        .setMessage("Eception: " + t.message)
                        .show()
            }
        })


    }

    private fun deleteMessage(id : String) {
        if(!isInternetConnection()) {
            Toast.makeText(this@MainActivity, "No internet connection!",
                    Toast.LENGTH_LONG)
                    .show()
            return
        }
        val json = getJSON()

        val call = json.deletePost(id)
        call.enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Error")
                        .setMessage("Eception: " + t.message)
                        .show()
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Toast.makeText(this@MainActivity,
                        "Post deleted from server!" + response.body().toString(),
                        Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun getJSON() : JSONChatInterface {

        val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val messageAPI = retrofit.create<JSONChatInterface>(JSONChatInterface::class.java)
        return messageAPI
    }

    private fun removePosts() {
        if(toDeleteList.isNotEmpty()) {
            toDeleteList.forEach { el -> deleteMessage(el) }
            toDeleteList.clear()
        }
    }

    @Suppress("DEPRECATION")
    private fun isInternetConnection() : Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    private fun goToSettings() {
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }
}

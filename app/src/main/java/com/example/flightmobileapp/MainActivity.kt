package com.example.flightmobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flightmobileapp.network.SimulatorApiService
import com.example.flightmobileapp.network.connectServer
import com.example.flightmobileapp.overview.SimulatorViewModel
import com.example.flightmobileapp.overview.User
import com.example.flightmobileapp.overview.UsersDataBase
import com.example.flightmobileapp.overview.UsersDataDao
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private val uiSocpe = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        connectBtn.setOnClickListener {
            uiSocpe.launch { connectionProcess() }
        }

        url.setOnClickListener{
            errMsg.visibility = View.INVISIBLE
        }

    }



    suspend fun connectionProcess() {
        try {

            val insertedUrl = url.text.toString()
            val simulatorApiService: SimulatorApiService = connectServer(insertedUrl)
            val response = simulatorApiService.getImg()
            response.await()
            //if we got the picture enter the url to the DB
            addToDB(url.text.toString())

        } catch (e:Exception) {
            errMsg.visibility = View.VISIBLE
            return
        }
        //if there was not any error in the connection we move to the next activity
        val intent = Intent(this, SimulatorActivity::class.java)
        intent.putExtra("url" ,url.text.toString())
        startActivity(intent)
    }

    suspend fun addToDB(url : String) {
        val db: UsersDataBase = UsersDataBase.getInstance(applicationContext)
        val newUser = User(0,url)
        db.userDatabaseDao.deleteByUrl(url)
        db.userDatabaseDao.insert(newUser)
    }

    override fun onStart() {
        super.onStart()
        //Update the last 5 users connected
        val db: UsersDataBase = UsersDataBase.getInstance(applicationContext)
        uiSocpe.launch {
            try {
                val users:List<User> = db.userDatabaseDao.getTable()
                txt1.text = users[0].url
                txt2.text = users[1].url
                txt3.text = users[2].url
                txt4.text = users[3].url
                txt5.text = users[4].url

            } catch (e:Exception) {}
        }
    }

}


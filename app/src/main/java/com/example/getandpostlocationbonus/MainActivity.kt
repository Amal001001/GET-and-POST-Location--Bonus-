package com.example.getandpostlocationbonus

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var et1:EditText
    lateinit var et2:EditText
    lateinit var button1: Button

    lateinit var et3:EditText
    lateinit var button2: Button
    lateinit var tv:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et1 = findViewById(R.id.et1)
        et2 = findViewById(R.id.et2)
        button1 = findViewById(R.id.button1)
        button1.setOnClickListener { addUser() }

        et3 = findViewById(R.id.et3)
        button2 = findViewById(R.id.button2)
        tv = findViewById(R.id.tv)
        button2.setOnClickListener { getUserLocation() }

    }

    fun addUser(){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (et1.text != null && et2.text != null) {
            // Hide Keyboard
            val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            val addedName = et1.text.toString()
            val addedLocation = et2.text.toString()
            et1.text.clear()
            et2.text.clear()

            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage("Please wait")
            progressDialog.show()

            apiInterface?.addUser(userInfo(addedName,addedLocation))!!.enqueue(object : Callback<userInfo> {
                override fun onResponse(call: Call<userInfo>, response: Response<userInfo>) {
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "$addedName added", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<userInfo>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "something went wrong!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun getUserLocation(){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<ArrayList<usersItem>?>? = apiInterface!!.getUserLocation()

        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        call?.enqueue(object : Callback<ArrayList<usersItem>?> {
            override fun onResponse(call: Call<ArrayList<usersItem>?>?, response: Response<ArrayList<usersItem>?>?) {
                progressDialog.dismiss()

                val resource: ArrayList<usersItem>? = response?.body()
                val allUsers = resource

                if (et3.text != null) {
                    for (user in allUsers!!) {
                      if (et3.text.toString().lowercase() == user.name.toString().lowercase())
                       tv.text = "${user.name.toString()}'s location is in: ${user.location.toString()}"
                    }
                }
                Log.d("TAG", "found location")
            }
            override fun onFailure(call: Call<ArrayList<usersItem>?>?, t: Throwable?) {
                call?.cancel()
                Log.d("TAG", "onFailure")
            }
        })
    }

}
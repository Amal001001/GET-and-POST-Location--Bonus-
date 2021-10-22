package com.example.getandpostlocationbonus

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {
    @GET("/test/")
    fun getUserLocation(): Call<ArrayList<usersItem>?>?

    @POST("/test/")
    fun addUser(@Body newUser:userInfo): Call<userInfo>
}
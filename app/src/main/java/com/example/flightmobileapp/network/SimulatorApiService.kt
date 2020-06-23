package com.example.flightmobileapp.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Response.success
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface SimulatorApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/command")
    fun sendCommand(@Body userData: SimulatorProperty):
            Deferred<ResponseBody>

    @GET("/api/screenshot")
    fun getImg():Deferred<ResponseBody>

}
@Throws
fun connectServer(url:String): SimulatorApiService {
        Log.i("msg", "connect server")
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(url)
            .build()
        val simulatorApiService =  retrofit.create(SimulatorApiService::class.java)
        return simulatorApiService

}

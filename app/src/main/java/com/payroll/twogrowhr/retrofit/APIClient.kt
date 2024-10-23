package com.payroll.twogrowhr.retrofit


import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {

    private var retrofit: Retrofit? = null

    fun clearRetrofitSession() {
        retrofit = null
    }

    fun getRetrofitConfigClient(mURL: String): Retrofit
    {
        Log.d("URL Inside getRetrofitConfigClient", mURL)
        return getRetrofitClient(mURL)
    }

    private fun getRetrofitClient(baseURL: String): Retrofit
    {
        val gson = GsonBuilder().setLenient().create()
        Log.d("URL Inside getRetrofit Client", baseURL)
        if (retrofit == null)
        {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }


}
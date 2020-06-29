package com.mythio.retrofitsample.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url


//private const val BASE_URL = "https://api.nasa.gov/planetary/"
private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

interface NasaApiService {

    @GET("apod?")
    fun getData(@Query("api_key") key: String,@Query("date") date: String? =null): Call<NasaAPOD>

    @Streaming
    @GET
    fun downloadFilefromUrl(@Url fileUrl: String): Call<ResponseBody>

}

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}
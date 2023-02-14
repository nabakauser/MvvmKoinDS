package com.example.mvvmkoinds.service

import com.example.mvvmkoinds.data.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("popular?")
    suspend fun getPopularMovies(@Query("api_key") api_key: String,
    ): Response<Movies>

}
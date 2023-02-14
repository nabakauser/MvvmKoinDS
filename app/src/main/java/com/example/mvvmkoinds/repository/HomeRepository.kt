package com.example.mvvmkoinds.repository

import com.example.mvvmkoin.utils.Resource
import com.example.mvvmkoinds.data.mapper.MoviesMapper
import com.example.mvvmkoinds.data.model.Movies
import com.example.mvvmkoinds.data.model.Result
import com.example.mvvmkoinds.manager.DataStoreManager
import com.example.mvvmkoinds.service.ApiService
import com.example.mvvmkoinds.warehouse.Constants.API_KEY

class HomeRepository (
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService
    ) {

    fun getSavedMovies(): MutableList<Result> {
        return dataStoreManager.getMovieList()
    }

    suspend fun saveMovie(movie: Result) {
        dataStoreManager.saveMovieList(movie)
    }

    suspend fun removeMovie(movie: Result) {
        dataStoreManager.removeMovieList(movie)
    }

    suspend fun getAllMovies(): Resource<Movies> {
        return MoviesMapper.map(apiService.getPopularMovies(API_KEY))
    }
}


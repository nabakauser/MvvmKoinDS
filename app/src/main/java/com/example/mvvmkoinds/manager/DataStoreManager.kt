package com.example.mvvmkoinds.manager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.example.mvvmkoinds.data.model.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("movie_model")

class DataStoreManager(private val context: Context) {

    init {
        context
    }

    companion object {
        val MOVIE_DETAILS = stringPreferencesKey("MOVIE_KEY")
    }

    private suspend fun storeMovieDetails(userData: List<Result>) {
        val movies = Gson().toJson(userData)
        context.dataStore.edit {
            it[MOVIE_DETAILS] = movies
        }
    }

    private val movieDetails: Flow<String> = context.dataStore.data.map {
        it[MOVIE_DETAILS] ?: ""
    }

    fun getMovieList(): MutableList<Result> {
        var savedList: List<Result> = mutableListOf()
        val movie = movieDetails.asLiveData().value
        if (movie?.isNotEmpty() == true) {
            val type = object : TypeToken<ArrayList<Result>>() {}.type
            savedList = Gson().fromJson<ArrayList<Result>>(movie, type)

        }
        return savedList.toMutableList()
    }

    suspend fun saveMovieList(movieData: Result) {
        val getList = getMovieList()
        val movieModel = getList.find { it.id == movieData.id }
        if (movieModel == null) {
            movieData.let { getList.add(it) }
            storeMovieDetails(getList)
        }
    }

    suspend fun removeMovieList(movieData: Result?) {
        val getList = getMovieList()
        val movieItemToRemove = getList.find {
            it.id == movieData?.id
        }
        if (movieItemToRemove != null) {
            movieData.let { getList.remove(movieItemToRemove) }
        }
    }
}
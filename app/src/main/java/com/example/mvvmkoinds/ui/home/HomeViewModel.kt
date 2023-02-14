package com.example.mvvmkoinds.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.mvvmkoin.utils.NetworkHelper
import com.example.mvvmkoin.utils.Status
import com.example.mvvmkoinds.R
import kotlinx.coroutines.launch
import com.example.mvvmkoinds.data.model.Result
import com.example.mvvmkoinds.repository.HomeRepository

class HomeViewModel (
    private val homeRepository: HomeRepository,
    private val networkHelper: NetworkHelper
    ) : ViewModel() {

    private val movies: ArrayList<Result> = arrayListOf()

    private val successLD = MutableLiveData<List<Result>>()
    val success : LiveData<List<Result>> = successLD.map { it }

    private val messageLD = MutableLiveData<StringType>()
    val message: LiveData<StringType> = messageLD

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                homeRepository.getAllMovies().let {
                    if (it.status == Status.SUCCESS) {
                        it.data?.results?.let { it1 -> movies.addAll(it1) }
                        checkDataSavedInDb(movies)
                        successLD.value = it.data?.results

                    } else if (it.status == Status.ERROR){
                        messageLD.value = StringType.StringRaw(it.msg.toString())
                    }
                }
            } else messageLD.value = StringType.StringResource(R.string.msg_no_internet)
        }
    }

    fun filterMovieList(searchText: String) {
        if (searchText.isEmpty()) {
            successLD.value = movies
        }else {
            val filteredList = movies.filter {
                it.title.contains(searchText,true)
            }
            successLD.value = filteredList
        }
    }

    fun onFavouritesClicked(movie: Result) {
        viewModelScope.launch {
            if(!movie.isSaved) {
                movie.isSaved = true
                homeRepository.saveMovie(movie)
                messageLD.value = StringType.StringResource(R.string.msg_saved)

            } else {
                movie.isSaved = false
                homeRepository.removeMovie(movie)
                messageLD.value = StringType.StringResource(R.string.msg_removed)
            }
        }

    }

    private fun checkDataSavedInDb(movieList : List<Result>) {
        movieList.forEach { movie ->
            val savedList = homeRepository.getSavedMovies()
            savedList.forEach {
                if(it.id == movie.id) {
                    movie.isSaved = true
                }
            }
        }
    }
}

sealed class StringType{
    class StringResource(@StringRes val  id : Int) : StringType()
    class StringRaw(val message : String) : StringType()
}
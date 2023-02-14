package com.example.mvvmkoinds.ui.favourites

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.mvvmkoin.utils.NetworkHelper
import com.example.mvvmkoin.utils.Status
import com.example.mvvmkoinds.R
import com.example.mvvmkoinds.data.model.Result
import com.example.mvvmkoinds.repository.HomeRepository
import kotlinx.coroutines.launch

class FavouritesViewModel (
    private val homeRepository: HomeRepository,
    ): ViewModel() {

    private val savedMoviesLD = MutableLiveData<List<Result>>()
    val  savedMovies: LiveData<List<Result>> = savedMoviesLD

    private val messageLD = MutableLiveData<StringType>()
    val message: LiveData<StringType> = messageLD

    init {
        getSavedMovies()
    }

    fun getSavedMovies() {
        val response = homeRepository.getSavedMovies()
        savedMoviesLD.value = response
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
}

sealed class StringType{
    class StringResource(@StringRes val  id : Int) : StringType()
    class StringRaw(val message : String) : StringType()
}

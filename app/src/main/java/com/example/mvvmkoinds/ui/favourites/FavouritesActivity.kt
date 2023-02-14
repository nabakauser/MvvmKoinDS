package com.example.mvvmkoinds.ui.favourites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mvvmkoinds.R
import com.example.mvvmkoinds.adapter.MovieAdapter
import com.example.mvvmkoinds.data.model.Result
import com.example.mvvmkoinds.databinding.ActivityFavouritesBinding
import com.example.mvvmkoinds.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesActivity : AppCompatActivity() {

    private var binding: ActivityFavouritesBinding? = null
    private val favouritesViewModel: FavouritesViewModel by viewModel()
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(
            movieList = arrayListOf(),
            onItemClick = {},
            onFavouritesClicked = {
                favouritesViewModel.onFavouritesClicked(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpUi()
        setUpObserver()
    }

    private fun setUpUi() {
        binding?.uiRvFavourites?.apply {
            adapter = movieAdapter
        }
    }

    private fun setMovieListToUi(movies : List<Result>) {
        movieAdapter.onUserListChanged(movies)
    }

    private fun setUpObserver() {
        favouritesViewModel.message.observe(this) { message ->
            when (message) {
                is StringType.StringResource -> {
                    Toast.makeText(this, getString(message.id), Toast.LENGTH_SHORT).show()
                }
                is StringType.StringRaw -> {
                    Toast.makeText(this, message.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        favouritesViewModel.savedMovies.observe(this) { savedMovies ->
            setMovieListToUi(savedMovies)
        }
    }
}
package com.example.mvvmkoinds.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.mvvmkoinds.R
import com.example.mvvmkoinds.adapter.MovieAdapter
import com.example.mvvmkoinds.data.model.Result
import com.example.mvvmkoinds.databinding.ActivityMainBinding
import com.example.mvvmkoinds.ui.favourites.FavouritesActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val homeViewModel: HomeViewModel by viewModel()
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(
            movieList = arrayListOf(),
            onItemClick = {},
            onFavouritesClicked = {
                    homeViewModel.onFavouritesClicked(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpUi()
        setUpListeners()
        setUpObserver()
    }

    private fun setUpUi() {
        binding?.uiRvMovies?.apply {
            adapter = movieAdapter
        }
    }

    private fun setUpListeners() {
        binding?.uiEtSearchBar?.doOnTextChanged { text, _, _, _ ->
            homeViewModel.filterMovieList(text.toString())
        }
        binding?.uiSwipeRefresh?.setOnRefreshListener {
            binding?.uiSwipeRefresh?.isRefreshing = false
            homeViewModel.fetchMovies()
        }
    }

    private fun setUpObserver() {
        homeViewModel.message.observe(this){ message ->
            when (message) {
                is StringType.StringResource -> {
                    Toast.makeText(this,getString(message.id), Toast.LENGTH_SHORT).show()
                }
                is StringType.StringRaw -> {
                    Toast.makeText(this,message.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        homeViewModel.success.observe(this) { movies ->
            binding?.uiProgressBar?.visibility = View.GONE
            movies?.let { movie -> setMovieListToUi(movie) }
        }
        homeViewModel.message.observe(this) {
            binding?.uiProgressBar?.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.uiFavourites) {
            navigateToFavouritesActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToFavouritesActivity() {
        val intent = Intent(this, FavouritesActivity::class.java)
        startActivity(intent)
    }

    private fun setMovieListToUi(movies : List<Result>) {
        movieAdapter.onUserListChanged(movies)
    }
}
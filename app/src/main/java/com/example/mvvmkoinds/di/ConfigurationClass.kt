package com.example.mvvmkoinds.di

import com.example.mvvmkoin.utils.NetworkHelper
import com.example.mvvmkoinds.RestHelper
import com.example.mvvmkoinds.manager.DataStoreManager
import com.example.mvvmkoinds.ui.favourites.FavouritesViewModel
import com.example.mvvmkoinds.repository.HomeRepository
import com.example.mvvmkoinds.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ConfigurationClass {
    fun modules() = repositoryModule + viewModelModule + commonModule
}

val repositoryModule = module {
    single { HomeRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { FavouritesViewModel(get()) }
}

val commonModule = module {
    single { NetworkHelper(androidContext()) }
    single { RestHelper.client }
    single { DataStoreManager(androidContext()) }
}

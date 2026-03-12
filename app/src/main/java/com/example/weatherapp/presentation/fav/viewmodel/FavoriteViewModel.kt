package com.example.weatherapp.presentation.fav.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.weather.WeatherRepository
import com.example.weatherapp.data.weather.model.FavoriteLocation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: WeatherRepository) : ViewModel() {

    val favorites: StateFlow<List<FavoriteLocation>> = repository
        .getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addFavorite(location: FavoriteLocation) {
        viewModelScope.launch { repository.addFavorite(location) }
    }

    fun removeFavorite(location: FavoriteLocation) {
        viewModelScope.launch { repository.removeFavorite(location) }
    }
}

@Suppress("UNCHECKED_CAST")
class FavoriteFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
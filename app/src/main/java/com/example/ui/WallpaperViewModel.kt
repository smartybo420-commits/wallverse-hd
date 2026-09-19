package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Wallpaper
import com.example.data.repository.WallpaperRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WallpaperViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WallpaperRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = WallpaperRepository(database)
    }

    val allWallpapers: StateFlow<List<Wallpaper>> = repository.allWallpapersFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteWallpapers: StateFlow<List<Wallpaper>> = repository.favoriteWallpapersFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val purchasedWallpapers: StateFlow<List<Wallpaper>> = repository.purchasedWallpapersFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val recentlyViewedWallpapers: StateFlow<List<Wallpaper>> = repository.recentlyViewedWallpapersFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedWallpaper = MutableStateFlow<Wallpaper?>(null)
    val selectedWallpaper: StateFlow<Wallpaper?> = _selectedWallpaper.asStateFlow()

    private val _isSearchOpen = MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _selectedCategoryForBrowse = MutableStateFlow<String?>(null)
    val selectedCategoryForBrowse: StateFlow<String?> = _selectedCategoryForBrowse.asStateFlow()

    fun selectWallpaper(wallpaper: Wallpaper?) {
        _selectedWallpaper.value = wallpaper
    }

    fun openSearch(open: Boolean) {
        _isSearchOpen.value = open
    }

    fun selectCategory(category: String?) {
        _selectedCategoryForBrowse.value = category
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        viewModelScope.launch {
            repository.toggleFavorite(wallpaper.id, wallpaper.isFavorite)
            // Update selected wallpaper instance if open
            if (_selectedWallpaper.value?.id == wallpaper.id) {
                _selectedWallpaper.value = _selectedWallpaper.value?.copy(isFavorite = !wallpaper.isFavorite)
            }
        }
    }

    fun recordViewed(wallpaper: Wallpaper) {
        viewModelScope.launch {
            repository.recordRecentlyViewed(wallpaper.id)
        }
    }

    fun purchaseWallpaper(wallpaper: Wallpaper, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val orderId = repository.recordPurchase(wallpaper.id, wallpaper.priceInr)
            if (_selectedWallpaper.value?.id == wallpaper.id) {
                _selectedWallpaper.value = _selectedWallpaper.value?.copy(isPurchased = true)
            }
            onComplete(orderId)
        }
    }

    fun addCustomWallpaper(
        title: String,
        category: String,
        resolution: String,
        isPremium: Boolean,
        priceInr: Int,
        isAnimated: Boolean,
        animationType: String?,
        imageUrl: String?
    ) {
        viewModelScope.launch {
            repository.addCustomWallpaper(
                title = title,
                category = category,
                resolution = resolution,
                isPremium = isPremium,
                priceInr = priceInr,
                isAnimated = isAnimated,
                animationType = animationType,
                imageUrl = imageUrl
            )
        }
    }
}

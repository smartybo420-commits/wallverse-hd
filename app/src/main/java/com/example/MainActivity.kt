package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.WallpaperViewModel
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WallpaperDetailScreen
import com.example.ui.theme.MyApplicationTheme

enum class NavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_tab_home"),
    CATEGORIES("Categories", Icons.Filled.GridView, Icons.Outlined.GridView, "nav_tab_categories"),
    FAVORITES("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "nav_tab_favorites"),
    PREMIUM("Premium", Icons.Filled.WorkspacePremium, Icons.Outlined.WorkspacePremium, "nav_tab_premium"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_tab_profile")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                WallVerseApp()
            }
        }
    }
}

@Composable
fun WallVerseApp(
    viewModel: WallpaperViewModel = viewModel()
) {
    val allWallpapers by viewModel.allWallpapers.collectAsStateWithLifecycle()
    val favoriteWallpapers by viewModel.favoriteWallpapers.collectAsStateWithLifecycle()
    val purchasedWallpapers by viewModel.purchasedWallpapers.collectAsStateWithLifecycle()
    val recentlyViewedWallpapers by viewModel.recentlyViewedWallpapers.collectAsStateWithLifecycle()
    val selectedWallpaper by viewModel.selectedWallpaper.collectAsStateWithLifecycle()
    val isSearchOpen by viewModel.isSearchOpen.collectAsStateWithLifecycle()
    val selectedCategoryForBrowse by viewModel.selectedCategoryForBrowse.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(NavTab.HOME) }

    // Android back navigation handler
    BackHandler(enabled = selectedWallpaper != null || isSearchOpen || currentTab != NavTab.HOME) {
        when {
            selectedWallpaper != null -> viewModel.selectWallpaper(null)
            isSearchOpen -> viewModel.openSearch(false)
            currentTab != NavTab.HOME -> currentTab = NavTab.HOME
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFF090A10),
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF111424),
                    contentColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    NavTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (isSearchOpen) viewModel.openSearch(false)
                                currentTab = tab
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = if (tab == NavTab.PREMIUM) "👑 ${tab.title}" else tab.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = if (tab == NavTab.PREMIUM) Color(0xFFF59E0B) else Color(0xFF38BDF8),
                                selectedTextColor = if (tab == NavTab.PREMIUM) Color(0xFFF59E0B) else Color(0xFF38BDF8),
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B),
                                indicatorColor = if (tab == NavTab.PREMIUM) Color(0x33F59E0B) else Color(0x3338BDF8)
                            ),
                            modifier = Modifier.testTag(tab.tag)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        NavTab.HOME -> HomeScreen(
                            wallpapers = allWallpapers,
                            onWallpaperClick = { viewModel.selectWallpaper(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onOpenSearch = { viewModel.openSearch(true) },
                            onNavigateToCategory = { category ->
                                viewModel.selectCategory(category)
                                currentTab = NavTab.CATEGORIES
                            }
                        )

                        NavTab.CATEGORIES -> CategoriesScreen(
                            wallpapers = allWallpapers,
                            onWallpaperClick = { viewModel.selectWallpaper(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            initialCategory = selectedCategoryForBrowse
                        )

                        NavTab.FAVORITES -> FavoritesScreen(
                            favoriteWallpapers = favoriteWallpapers,
                            recentlyViewedWallpapers = recentlyViewedWallpapers,
                            onWallpaperClick = { viewModel.selectWallpaper(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                        )

                        NavTab.PREMIUM -> PremiumScreen(
                            premiumWallpapers = allWallpapers.filter { it.isPremium },
                            purchasedWallpapers = purchasedWallpapers,
                            onWallpaperClick = { viewModel.selectWallpaper(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                        )

                        NavTab.PROFILE -> ProfileScreen(
                            favoriteCount = favoriteWallpapers.size,
                            purchasedCount = purchasedWallpapers.size,
                            onAddNewWallpaper = { title, category, resolution, isPremium, priceInr, isAnimated, animType, url ->
                                viewModel.addCustomWallpaper(
                                    title = title,
                                    category = category,
                                    resolution = resolution,
                                    isPremium = isPremium,
                                    priceInr = priceInr,
                                    isAnimated = isAnimated,
                                    animationType = animType,
                                    imageUrl = url
                                )
                            }
                        )
                    }
                }
            }
        }

        // Full Screen Search Overlay
        AnimatedVisibility(
            visible = isSearchOpen,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        ) {
            SearchScreen(
                wallpapers = allWallpapers,
                onWallpaperClick = { viewModel.selectWallpaper(it) },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                onBack = { viewModel.openSearch(false) }
            )
        }

        // Full Screen Wallpaper Detail Overlay
        AnimatedVisibility(
            visible = selectedWallpaper != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            selectedWallpaper?.let { wp ->
                WallpaperDetailScreen(
                    wallpaper = wp,
                    onBack = { viewModel.selectWallpaper(null) },
                    onToggleFavorite = { viewModel.toggleFavorite(wp) },
                    onPurchaseConfirmed = { orderId ->
                        viewModel.purchaseWallpaper(wp) { }
                    },
                    onRecordViewed = {
                        viewModel.recordViewed(wp)
                    }
                )
            }
        }
    }
}

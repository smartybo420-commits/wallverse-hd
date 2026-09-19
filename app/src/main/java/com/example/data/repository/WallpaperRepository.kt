package com.example.data.repository

import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.RecentlyViewedEntity
import com.example.data.local.UserFavoriteEntity
import com.example.data.local.UserPurchaseEntity
import com.example.data.local.WallpaperEntity
import com.example.data.model.Wallpaper
import com.example.data.model.WallpaperCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class WallpaperRepository(private val database: AppDatabase) {

    private val dao = database.wallpaperDao()

    // Core Built-in Curated Catalog
    private val defaultCatalog = listOf(
        // Generated High-Res Assets
        Wallpaper(
            id = "wp_nature_aurora",
            title = "Emerald Aurora Fjords",
            category = WallpaperCategory.NATURE.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "5.2 MB",
            isPremium = false,
            priceInr = 0,
            drawableResId = R.drawable.img_wp_nature_aurora,
            primaryColorHex = 0xFF10B981,
            secondaryColorHex = 0xFF059669,
            downloads = 34500,
            views = 98200,
            author = "Nordic Lens Studios",
            isFeatured = true,
            isTrending = true,
            isNew = false
        ),
        Wallpaper(
            id = "wp_cosmic_nebula",
            title = "Orion Stardust Nebula",
            category = WallpaperCategory.SPACE.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "6.1 MB",
            isPremium = true,
            priceInr = 20,
            drawableResId = R.drawable.img_wp_cosmic_nebula,
            primaryColorHex = 0xFF8B5CF6,
            secondaryColorHex = 0xFF6366F1,
            downloads = 48200,
            views = 124000,
            author = "DeepSky Observatory",
            isFeatured = true,
            isTrending = true,
            isNew = true
        ),
        Wallpaper(
            id = "wp_cyber_car",
            title = "Cyberpunk Phantom GT",
            category = WallpaperCategory.CARS.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "5.8 MB",
            isPremium = true,
            priceInr = 50,
            drawableResId = R.drawable.img_wp_cyber_car,
            primaryColorHex = 0xFFEF4444,
            secondaryColorHex = 0xFFF97316,
            downloads = 28900,
            views = 87400,
            author = "Apex Concept Design",
            isFeatured = true,
            isTrending = true,
            isNew = false
        ),
        Wallpaper(
            id = "wp_amoled_dragon",
            title = "OLED Cyber Dragon",
            category = WallpaperCategory.AMOLED.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "3.9 MB",
            isPremium = true,
            priceInr = 10,
            drawableResId = R.drawable.img_wp_amoled_dragon,
            primaryColorHex = 0xFFF59E0B,
            secondaryColorHex = 0xFF06B6D4,
            downloads = 51200,
            views = 159000,
            author = "Monochrome Masters",
            isFeatured = false,
            isTrending = true,
            isNew = true
        ),

        // Live Animated Wallpapers
        Wallpaper(
            id = "anim_cosmic_vortex",
            title = "Cosmic Warp & Stardust",
            category = WallpaperCategory.SPACE.title,
            resolution = "60 FPS Live Canvas",
            fileSize = "Live Animation",
            isPremium = false,
            priceInr = 0,
            isAnimated = true,
            animationType = "COSMIC_WARP",
            primaryColorHex = 0xFF7C3AED,
            secondaryColorHex = 0xFF38BDF8,
            downloads = 63200,
            views = 192000,
            author = "WallVerse Interactive Lab",
            isFeatured = true,
            isTrending = true,
            isNew = true
        ),
        Wallpaper(
            id = "anim_neon_aurora",
            title = "Bioluminescent Aurora Waves",
            category = WallpaperCategory.NATURE.title,
            resolution = "60 FPS Live Canvas",
            fileSize = "Live Animation",
            isPremium = true,
            priceInr = 10,
            isAnimated = true,
            animationType = "AURORA",
            primaryColorHex = 0xFF10B981,
            secondaryColorHex = 0xFF06B6D4,
            downloads = 41500,
            views = 118000,
            author = "Fluid Dynamics Art",
            isFeatured = false,
            isTrending = true,
            isNew = true
        ),
        Wallpaper(
            id = "anim_amoled_matrix",
            title = "AMOLED Digital Quantum Grid",
            category = WallpaperCategory.AMOLED.title,
            resolution = "60 FPS Live Canvas",
            fileSize = "Live Animation",
            isPremium = false,
            priceInr = 0,
            isAnimated = true,
            animationType = "AMOLED_MATRIX",
            primaryColorHex = 0xFF06B6D4,
            secondaryColorHex = 0xFF8B5CF6,
            downloads = 57100,
            views = 175000,
            author = "OLED Core Interactive",
            isFeatured = false,
            isTrending = true,
            isNew = false
        ),
        Wallpaper(
            id = "anim_quantum_pulse",
            title = "Quantum Pulsar Plasma",
            category = WallpaperCategory.ABSTRACT.title,
            resolution = "60 FPS Live Canvas",
            fileSize = "Live Animation",
            isPremium = true,
            priceInr = 5,
            isAnimated = true,
            animationType = "QUANTUM_PULSE",
            primaryColorHex = 0xFFF43F5E,
            secondaryColorHex = 0xFF8B5CF6,
            downloads = 22400,
            views = 69000,
            author = "Plasma Dynamics",
            isFeatured = false,
            isTrending = false,
            isNew = true
        ),
        Wallpaper(
            id = "anim_fireflies",
            title = "Mystic Forest Fireflies",
            category = WallpaperCategory.NATURE.title,
            resolution = "60 FPS Live Canvas",
            fileSize = "Live Animation",
            isPremium = false,
            priceInr = 0,
            isAnimated = true,
            animationType = "FIREFLIES",
            primaryColorHex = 0xFFFBBF24,
            secondaryColorHex = 0xFF059669,
            downloads = 38900,
            views = 104000,
            author = "Enchanted Nature Lab",
            isFeatured = false,
            isTrending = false,
            isNew = true
        ),

        // Rich Curated Wallpapers for all remaining categories
        Wallpaper(
            id = "wp_gaming_mecha",
            title = "Neon Mecha Ronin",
            category = WallpaperCategory.GAMING.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "4.8 MB",
            isPremium = true,
            priceInr = 20,
            primaryColorHex = 0xFF8B5CF6,
            secondaryColorHex = 0xFFEC4899,
            downloads = 32100,
            views = 94000,
            author = "CyberPixel Studios",
            isFeatured = false,
            isTrending = true,
            isNew = true
        ),
        Wallpaper(
            id = "wp_anime_sakura",
            title = "Spirit Blossom Blade",
            category = WallpaperCategory.ANIME.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "4.2 MB",
            isPremium = false,
            priceInr = 0,
            primaryColorHex = 0xFFF43F5E,
            secondaryColorHex = 0xFFFB7185,
            downloads = 68400,
            views = 210000,
            author = "MangaVerse Arts",
            isFeatured = false,
            isTrending = true,
            isNew = false
        ),
        Wallpaper(
            id = "wp_animals_lion",
            title = "Golden Serengeti Monarch",
            category = WallpaperCategory.ANIMALS.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "5.1 MB",
            isPremium = true,
            priceInr = 10,
            primaryColorHex = 0xFFD97706,
            secondaryColorHex = 0xFFF59E0B,
            downloads = 29300,
            views = 84000,
            author = "WildAfrica Visuals",
            isFeatured = false,
            isTrending = false,
            isNew = true
        ),
        Wallpaper(
            id = "wp_abstract_prism",
            title = "Prismatic Obsidian Geometry",
            category = WallpaperCategory.ABSTRACT.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "4.4 MB",
            isPremium = false,
            priceInr = 0,
            primaryColorHex = 0xFF9333EA,
            secondaryColorHex = 0xFF38BDF8,
            downloads = 41200,
            views = 129000,
            author = "Studio Refract",
            isFeatured = false,
            isTrending = true,
            isNew = false
        ),
        Wallpaper(
            id = "wp_festivals_lights",
            title = "Celestial Lantern Constellation",
            category = WallpaperCategory.FESTIVALS.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "5.3 MB",
            isPremium = true,
            priceInr = 5,
            primaryColorHex = 0xFFE11D48,
            secondaryColorHex = 0xFFF59E0B,
            downloads = 19400,
            views = 62000,
            author = "Global Celebrations Art",
            isFeatured = false,
            isTrending = false,
            isNew = true
        ),
        Wallpaper(
            id = "wp_tech_quantum",
            title = "Superconducting Quantum Core",
            category = WallpaperCategory.TECHNOLOGY.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "4.7 MB",
            isPremium = false,
            priceInr = 0,
            primaryColorHex = 0xFF0284C7,
            secondaryColorHex = 0xFF06B6D4,
            downloads = 36800,
            views = 112000,
            author = "NanoSilicon Tech",
            isFeatured = false,
            isTrending = true,
            isNew = false
        ),
        Wallpaper(
            id = "wp_movies_interstellar",
            title = "Gargantua Singularity Event",
            category = WallpaperCategory.MOVIES.title,
            resolution = "4K UHD (2160×3840)",
            fileSize = "5.6 MB",
            isPremium = true,
            priceInr = 50,
            primaryColorHex = 0xFF4F46E5,
            secondaryColorHex = 0xFFF59E0B,
            downloads = 54000,
            views = 171000,
            author = "CinemaWarp FX",
            isFeatured = false,
            isTrending = true,
            isNew = true
        )
    )

    // Flow combining catalog, custom DB wallpapers, favorite states, and purchase states
    val allWallpapersFlow: Flow<List<Wallpaper>> = combine(
        dao.getAllCustomWallpapers(),
        dao.getFavoriteIds(),
        dao.getPurchasedIds()
    ) { customEntities, favIds, purchasedIds ->
        val customWallpapers = customEntities.map { entity ->
            Wallpaper(
                id = entity.id,
                title = entity.title,
                category = entity.category,
                resolution = entity.resolution,
                fileSize = entity.fileSize,
                isPremium = entity.isPremium,
                priceInr = entity.priceInr,
                isAnimated = entity.isAnimated,
                animationType = entity.animationType,
                imageUrl = entity.imageUrl,
                primaryColorHex = entity.primaryColorHex,
                secondaryColorHex = entity.secondaryColorHex,
                downloads = entity.downloads,
                views = entity.views,
                author = entity.author,
                license = entity.license,
                isFeatured = false,
                isTrending = false,
                isNew = true,
                isPurchased = !entity.isPremium || purchasedIds.contains(entity.id),
                isFavorite = favIds.contains(entity.id)
            )
        }

        val mappedDefaults = defaultCatalog.map { item ->
            item.copy(
                isPurchased = !item.isPremium || purchasedIds.contains(item.id),
                isFavorite = favIds.contains(item.id)
            )
        }

        mappedDefaults + customWallpapers
    }

    val favoriteWallpapersFlow: Flow<List<Wallpaper>> = combine(
        allWallpapersFlow,
        dao.getFavoriteIds()
    ) { all, favIds ->
        all.filter { favIds.contains(it.id) }
    }

    val purchasedWallpapersFlow: Flow<List<Wallpaper>> = combine(
        allWallpapersFlow,
        dao.getPurchasedIds()
    ) { all, purchasedIds ->
        all.filter { it.isPremium && purchasedIds.contains(it.id) }
    }

    val recentlyViewedWallpapersFlow: Flow<List<Wallpaper>> = combine(
        allWallpapersFlow,
        dao.getRecentlyViewedIds()
    ) { all, recentIds ->
        val wallpaperMap = all.associateBy { it.id }
        recentIds.mapNotNull { wallpaperMap[it] }
    }

    suspend fun toggleFavorite(wallpaperId: String, currentIsFav: Boolean) {
        if (currentIsFav) {
            dao.removeFavorite(wallpaperId)
        } else {
            dao.addFavorite(UserFavoriteEntity(wallpaperId = wallpaperId))
        }
    }

    suspend fun recordRecentlyViewed(wallpaperId: String) {
        dao.recordRecentlyViewed(RecentlyViewedEntity(wallpaperId = wallpaperId))
    }

    suspend fun recordPurchase(wallpaperId: String, price: Int): String {
        val orderId = "GPA.${(1000..9999).random()}-${(1000..9999).random()}-${(1000..9999).random()}"
        dao.recordPurchase(
            UserPurchaseEntity(
                wallpaperId = wallpaperId,
                pricePaid = price,
                orderId = orderId
            )
        )
        return orderId
    }

    suspend fun addCustomWallpaper(
        title: String,
        category: String,
        resolution: String = "4K UHD (2160×3840)",
        isPremium: Boolean,
        priceInr: Int,
        isAnimated: Boolean = false,
        animationType: String? = null,
        imageUrl: String? = null
    ): String {
        val id = "custom_wp_${UUID.randomUUID().toString().take(8)}"
        val entity = WallpaperEntity(
            id = id,
            title = title,
            category = category,
            resolution = resolution,
            fileSize = "4.2 MB",
            isPremium = isPremium,
            priceInr = if (isPremium) priceInr else 0,
            isAnimated = isAnimated,
            animationType = animationType,
            imageUrl = imageUrl,
            primaryColorHex = 0xFF8B5CF6,
            secondaryColorHex = 0xFF06B6D4,
            downloads = 1,
            views = 1,
            author = "WallVerse Creator / Community",
            license = "User Added / Verified"
        )
        dao.insertWallpaper(entity)
        return id
    }
}

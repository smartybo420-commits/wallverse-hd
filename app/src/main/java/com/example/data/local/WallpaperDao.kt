package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {

    // Custom / Dynamically added wallpapers
    @Query("SELECT * FROM wallpapers ORDER BY createdAt DESC")
    fun getAllCustomWallpapers(): Flow<List<WallpaperEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: WallpaperEntity)

    // Favorites
    @Query("SELECT wallpaperId FROM user_favorites ORDER BY addedAt DESC")
    fun getFavoriteIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM user_favorites WHERE wallpaperId = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(fav: UserFavoriteEntity)

    @Query("DELETE FROM user_favorites WHERE wallpaperId = :id")
    suspend fun removeFavorite(id: String)

    // Purchases (Permanent unlocking)
    @Query("SELECT * FROM user_purchases ORDER BY purchasedAt DESC")
    fun getAllPurchases(): Flow<List<UserPurchaseEntity>>

    @Query("SELECT wallpaperId FROM user_purchases")
    fun getPurchasedIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM user_purchases WHERE wallpaperId = :id)")
    fun isPurchased(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordPurchase(purchase: UserPurchaseEntity)

    // Recently Viewed
    @Query("SELECT wallpaperId FROM recently_viewed ORDER BY viewedAt DESC LIMIT 30")
    fun getRecentlyViewedIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordRecentlyViewed(item: RecentlyViewedEntity)

    @Query("DELETE FROM recently_viewed")
    suspend fun clearRecentlyViewed()
}

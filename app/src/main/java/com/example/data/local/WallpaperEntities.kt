package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
data class WallpaperEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val resolution: String,
    val fileSize: String,
    val isPremium: Boolean,
    val priceInr: Int,
    val isAnimated: Boolean,
    val animationType: String?,
    val imageUrl: String?,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val downloads: Int,
    val views: Int,
    val author: String,
    val license: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_favorites")
data class UserFavoriteEntity(
    @PrimaryKey val wallpaperId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_purchases")
data class UserPurchaseEntity(
    @PrimaryKey val wallpaperId: String,
    val pricePaid: Int,
    val orderId: String,
    val purchasedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "recently_viewed")
data class RecentlyViewedEntity(
    @PrimaryKey val wallpaperId: String,
    val viewedAt: Long = System.currentTimeMillis()
)

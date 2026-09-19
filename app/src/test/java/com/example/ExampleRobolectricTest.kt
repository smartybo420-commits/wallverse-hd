package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.repository.WallpaperRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var database: AppDatabase
  private lateinit var repository: WallpaperRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = WallpaperRepository(database)
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun read_string_from_context() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("WallVerse HD", appName)
  }

  @Test
  fun test_repository_default_catalog_and_features() = runBlocking {
    val all = repository.allWallpapersFlow.first()
    assertTrue(all.isNotEmpty())

    val liveWallpapers = all.filter { it.isAnimated }
    assertTrue("Should have animated/live wallpapers", liveWallpapers.isNotEmpty())

    val premiumWallpapers = all.filter { it.isPremium }
    assertTrue("Should have premium wallpapers", premiumWallpapers.isNotEmpty())

    // Test favorite toggle
    val firstItem = all.first()
    repository.toggleFavorite(firstItem.id, false)
    val favorites = repository.favoriteWallpapersFlow.first()
    assertTrue(favorites.any { it.id == firstItem.id })

    // Test in-app purchase unlock
    val firstPremium = premiumWallpapers.first()
    val orderId = repository.recordPurchase(firstPremium.id, firstPremium.priceInr)
    assertNotNull(orderId)
    val purchased = repository.purchasedWallpapersFlow.first()
    assertTrue(purchased.any { it.id == firstPremium.id })
  }
}


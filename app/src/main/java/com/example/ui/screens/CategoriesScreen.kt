package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Wallpaper
import com.example.data.model.WallpaperCategory
import com.example.ui.components.AdBannerCard
import com.example.ui.components.WallpaperCard

@Composable
fun CategoriesScreen(
    wallpapers: List<Wallpaper>,
    onWallpaperClick: (Wallpaper) -> Unit,
    onToggleFavorite: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier,
    initialCategory: String? = null
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var subFilter by remember { mutableStateOf("All") } // "All", "4K", "Free", "Premium"

    val categories = WallpaperCategory.values()

    if (selectedCategory != null) {
        // Detailed Category View
        val categoryWallpapers = remember(wallpapers, selectedCategory, subFilter) {
            val base = wallpapers.filter { it.category.equals(selectedCategory, ignoreCase = true) }
            when (subFilter) {
                "4K" -> base.filter { it.resolution.contains("4K", ignoreCase = true) }
                "Free" -> base.filter { !it.isPremium }
                "Premium" -> base.filter { it.isPremium }
                else -> base
            }
        }

        val categoryObj = categories.firstOrNull { it.title.equals(selectedCategory, ignoreCase = true) }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("category_detail_screen"),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { selectedCategory = null },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B2038))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to categories",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "${categoryObj?.iconEmoji ?: "📁"} ${selectedCategory}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            // Sub-filters row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "4K", "Free", "Premium").forEach { filter ->
                        val isSelected = subFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { subFilter = filter },
                            label = { Text(filter, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF141726),
                                labelColor = Color(0xFF94A3B8),
                                selectedContainerColor = Color(0xFF8B5CF6),
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Empty state check
            if (categoryWallpapers.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("✨", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No wallpapers under this filter yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            } else {
                items(categoryWallpapers.chunked(2)) { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        for (item in pair) {
                            WallpaperCard(
                                wallpaper = item,
                                onClick = { onWallpaperClick(item) },
                                onToggleFavorite = { onToggleFavorite(item) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    } else {
        // Main Categories Grid
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("categories_screen"),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Explore Categories",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFF8FAFC)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Handpicked themes crafted for OLED & high-density screens",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            items(categories.toList()) { category ->
                val count = wallpapers.count { it.category.equals(category.title, ignoreCase = true) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { selectedCategory = category.title }
                        .testTag("category_card_${category.name}"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141829)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(category.gradientStart).copy(alpha = 0.55f),
                                        Color(category.gradientEnd).copy(alpha = 0.25f),
                                        Color(0xFF141829)
                                    )
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33000000)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = category.iconEmoji, fontSize = 24.sp)
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {
                                    Text(
                                        text = category.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = if (count > 0) "$count Wallpapers" else "Explore Collection",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            item {
                AdBannerCard(
                    title = "Pro Wallpaper Engine",
                    subtitle = "Get 1,000+ exclusive 4K & animated live backgrounds",
                    ctaText = "Learn More"
                )
            }
        }
    }
}

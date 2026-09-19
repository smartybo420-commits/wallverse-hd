package com.example.data.model

data class Wallpaper(
    val id: String,
    val title: String,
    val category: String,
    val resolution: String = "4K UHD (2160×3840)",
    val fileSize: String = "4.5 MB",
    val isPremium: Boolean = false,
    val priceInr: Int = 0,
    val isAnimated: Boolean = false,
    val animationType: String? = null,
    val drawableResId: Int? = null,
    val imageUrl: String? = null,
    val primaryColorHex: Long = 0xFF8B5CF6,
    val secondaryColorHex: Long = 0xFF06B6D4,
    val downloads: Int = 1240,
    val views: Int = 3890,
    val author: String = "WallVerse Creative Lab",
    val license: String = "Royalty-Free / Original HD",
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false,
    val isNew: Boolean = false,
    val isPurchased: Boolean = false,
    val isFavorite: Boolean = false
) {
    val displayPrice: String
        get() = if (isPremium) "₹$priceInr" else "FREE"
}

enum class WallpaperCategory(val title: String, val iconEmoji: String, val gradientStart: Long, val gradientEnd: Long) {
    NATURE("Nature", "🌄", 0xFF059669, 0xFF10B981),
    CARS("Cars", "🚗", 0xFFDC2626, 0xFFF97316),
    GAMING("Gaming", "🎮", 0xFF7C3AED, 0xFFC026D3),
    ANIME("Anime", "⚔️", 0xFFDB2777, 0xFFF43F5E),
    SPACE("Space", "🌌", 0xFF4338CA, 0xFF6366F1),
    ANIMALS("Animals", "🦁", 0xFFD97706, 0xFFF59E0B),
    AMOLED("AMOLED", "🖤", 0xFF0F172A, 0xFF334155),
    ABSTRACT("Abstract", "🎨", 0xFF9333EA, 0xFFA855F7),
    FESTIVALS("Festivals", "🎆", 0xFFE11D48, 0xFFFB7185),
    TECHNOLOGY("Technology", "⚡", 0xFF0284C7, 0xFF06B6D4),
    MOVIES("Movies & Entertainment", "🎬", 0xFF4F46E5, 0xFF818CF8)
}

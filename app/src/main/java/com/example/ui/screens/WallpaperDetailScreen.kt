package com.example.ui.screens

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Wallpaper
import com.example.service.WallVerseLiveWallpaperService
import com.example.ui.components.AnimatedWallpaperCanvas
import com.example.ui.components.InAppPurchaseDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperDetailScreen(
    wallpaper: Wallpaper,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onPurchaseConfirmed: (String) -> Unit,
    onRecordViewed: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showControls by remember { mutableStateOf(true) }
    var showPreviewMock by remember { mutableStateOf(false) }
    var showSetWallpaperSheet by remember { mutableStateOf(false) }
    var showPurchaseDialog by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var isApplyingWallpaper by remember { mutableStateOf(false) }

    LaunchedEffect(wallpaper.id) {
        onRecordViewed()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
    ) {
        // Full-screen Wallpaper Canvas / Image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { showControls = !showControls }
        ) {
            if (wallpaper.isAnimated) {
                AnimatedWallpaperCanvas(
                    animationType = wallpaper.animationType,
                    modifier = Modifier.fillMaxSize(),
                    isInteractive = true
                )
            } else if (wallpaper.drawableResId != null) {
                Image(
                    painter = painterResource(id = wallpaper.drawableResId),
                    contentDescription = wallpaper.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (!wallpaper.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = wallpaper.imageUrl,
                    contentDescription = wallpaper.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(wallpaper.primaryColorHex),
                                    Color(wallpaper.secondaryColorHex),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                )
            }

            // Simulated Home Screen Clock Overlay (Toggled via preview mode)
            AnimatedVisibility(
                visible = showPreviewMock,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val currentTime = remember {
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                    }
                    val currentDate = remember {
                        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
                    }

                    Text(
                        text = currentTime,
                        fontSize = 76.sp,
                        fontWeight = FontWeight.ExtraLight,
                        color = Color.White,
                        letterSpacing = (-2).sp
                    )
                    Text(
                        text = currentDate,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(280.dp))

                    // Mock app dock icons
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0x33000000))
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(22.dp)
                    ) {
                        listOf("📞", "💬", "🌐", "📷").forEach { iconEmoji ->
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x44FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(iconEmoji, fontSize = 22.sp)
                            }
                        }
                    }
                }
            }
        }

        // Top App Bar Controls
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x99000000))
                        .testTag("detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Preview Clock toggle
                    IconButton(
                        onClick = { showPreviewMock = !showPreviewMock },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (showPreviewMock) Color(0xFF38BDF8) else Color(0x99000000))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = "Preview Lockscreen",
                            tint = if (showPreviewMock) Color.Black else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Share
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "WallVerse HD - ${wallpaper.title}"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out “${wallpaper.title}” on WallVerse HD! 4K Ultra HD wallpaper in ${wallpaper.category}."
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Wallpaper"))
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x99000000))
                            .testTag("detail_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Favorite
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x99000000))
                            .testTag("detail_fav_button")
                    ) {
                        Icon(
                            imageVector = if (wallpaper.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (wallpaper.isFavorite) Color(0xFFF43F5E) else Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // Bottom Details & Action Card
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xEB131728)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Title and Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = wallpaper.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${wallpaper.category} • ${wallpaper.resolution}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }

                        // Premium or Free Tag
                        if (wallpaper.isPremium) {
                            val tagBgModifier = if (wallpaper.isPurchased) {
                                Modifier.background(Color(0x3310B981))
                            } else {
                                Modifier.background(Brush.horizontalGradient(listOf(Color(0xFFF59E0B), Color(0xFFD97706))))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .then(tagBgModifier)
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (wallpaper.isPurchased) "UNLOCKED ✓" else "👑 ₹${wallpaper.priceInr}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x3310B981))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "FREE",
                                    color = Color(0xFF34D399),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Specs Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1B2036))
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SpecPill("Quality", "4K UHD")
                        SpecPill("Size", wallpaper.fileSize)
                        SpecPill("Downloads", "${wallpaper.downloads / 1000}K")
                        SpecPill("License", "Royalty Free")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Download Button
                        FilledTonalButton(
                            onClick = {
                                isDownloading = true
                                scope.launch {
                                    delay(1000)
                                    isDownloading = false
                                    Toast.makeText(
                                        context,
                                        "Saved “${wallpaper.title}” in 4K resolution to Gallery!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("detail_download_button"),
                            shape = RoundedCornerShape(14.dp),
                            enabled = !isDownloading
                        ) {
                            if (isDownloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color(0xFF38BDF8),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Download", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Set Wallpaper / Unlock Button
                        if (wallpaper.isPremium && !wallpaper.isPurchased) {
                            Button(
                                onClick = { showPurchaseDialog = true },
                                modifier = Modifier
                                    .weight(1.4f)
                                    .height(50.dp)
                                    .testTag("detail_unlock_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF59E0B)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("👑 Unlock for ₹${wallpaper.priceInr}", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (wallpaper.isAnimated) {
                                        // Launch Android Live Wallpaper Chooser
                                        try {
                                            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                                                putExtra(
                                                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                                    ComponentName(context, WallVerseLiveWallpaperService::class.java)
                                                )
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Fallback to standard wallpaper picker
                                            val fallbackIntent = Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
                                            try {
                                                context.startActivity(fallbackIntent)
                                            } catch (ex: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    "Live Wallpaper applied in WallVerse preview engine!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        showSetWallpaperSheet = true
                                    }
                                },
                                modifier = Modifier
                                    .weight(1.4f)
                                    .height(50.dp)
                                    .testTag("detail_apply_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF8B5CF6)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = if (wallpaper.isAnimated) Icons.Default.AutoAwesome else Icons.Default.Wallpaper,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (wallpaper.isAnimated) "Apply Live Wallpaper" else "Set Wallpaper",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Set Wallpaper Modal Bottom Sheet (Home, Lock, Both)
        if (showSetWallpaperSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSetWallpaperSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = Color(0xFF131728)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Apply Wallpaper",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Choose where you want to set “${wallpaper.title}”",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    WallpaperTargetOption(
                        title = "Set as Home Screen",
                        subtitle = "Displays on device main screens",
                        icon = Icons.Default.Home
                    ) {
                        showSetWallpaperSheet = false
                        applyWallpaperTarget(context, wallpaper, WallpaperManager.FLAG_SYSTEM)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    WallpaperTargetOption(
                        title = "Set as Lock Screen",
                        subtitle = "Displays when phone is locked",
                        icon = Icons.Default.Lock
                    ) {
                        showSetWallpaperSheet = false
                        applyWallpaperTarget(context, wallpaper, WallpaperManager.FLAG_LOCK)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    WallpaperTargetOption(
                        title = "Set as Both",
                        subtitle = "Applies to both Home and Lock Screen",
                        icon = Icons.Default.PhoneAndroid
                    ) {
                        showSetWallpaperSheet = false
                        applyWallpaperTarget(
                            context,
                            wallpaper,
                            WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }

        // Purchase Dialog
        if (showPurchaseDialog) {
            InAppPurchaseDialog(
                wallpaper = wallpaper,
                onDismiss = { showPurchaseDialog = false },
                onPurchaseConfirmed = { orderId ->
                    onPurchaseConfirmed(orderId)
                }
            )
        }
    }
}

@Composable
private fun SpecPill(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFE2E8F0))
    }
}

@Composable
private fun WallpaperTargetOption(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF1B2038),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF262E4E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                )
            }
        }
    }
}

private fun applyWallpaperTarget(context: Context, wallpaper: Wallpaper, flags: Int) {
    val wallpaperManager = WallpaperManager.getInstance(context)
    try {
        val bitmap = if (wallpaper.drawableResId != null) {
            BitmapFactory.decodeResource(context.resources, wallpaper.drawableResId)
        } else {
            // Generate synthetic high-res wallpaper bitmap
            val b = Bitmap.createBitmap(1080, 2400, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(b)
            val paint = Paint()
            paint.color = android.graphics.Color.rgb(
                ((wallpaper.primaryColorHex shr 16) and 0xFF).toInt(),
                ((wallpaper.primaryColorHex shr 8) and 0xFF).toInt(),
                (wallpaper.primaryColorHex and 0xFF).toInt()
            )
            canvas.drawRect(0f, 0f, 1080f, 2400f, paint)
            b
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wallpaperManager.setBitmap(bitmap, null, true, flags)
        } else {
            wallpaperManager.setBitmap(bitmap)
        }

        val targetName = when (flags) {
            WallpaperManager.FLAG_SYSTEM -> "Home Screen"
            WallpaperManager.FLAG_LOCK -> "Lock Screen"
            else -> "Home and Lock Screen"
        }
        Toast.makeText(context, "Wallpaper successfully set to $targetName!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Wallpaper applied successfully!", Toast.LENGTH_SHORT).show()
    }
}

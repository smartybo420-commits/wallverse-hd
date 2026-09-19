package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun AnimatedWallpaperCanvas(
    animationType: String?,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wallpaper_animation")
    val animTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animTime"
    )

    var touchOffset by remember { mutableFloatStateOf(0f) }
    var touchX by remember { mutableFloatStateOf(0.5f) }
    var touchY by remember { mutableFloatStateOf(0.5f) }

    val interactiveModifier = if (isInteractive) {
        modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    touchX = (offset.x / size.width).coerceIn(0f, 1f)
                    touchY = (offset.y / size.height).coerceIn(0f, 1f)
                    touchOffset += 1f
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    touchX = (change.position.x / size.width).coerceIn(0f, 1f)
                    touchY = (change.position.y / size.height).coerceIn(0f, 1f)
                }
            }
    } else {
        modifier
    }

    // Seeded star / particle cache
    val particles = remember {
        List(60) {
            SimulatedParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 3.5f + 1.2f,
                speed = Random.nextFloat() * 0.4f + 0.1f,
                phase = Random.nextFloat() * 6.28f
            )
        }
    }

    Canvas(modifier = interactiveModifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val t = animTime

        when (animationType) {
            "AURORA" -> {
                // Background dark night sky
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF030A14), Color(0xFF07192C), Color(0xFF0A1128))
                    )
                )

                // Render dynamic aurora ribbons
                for (layer in 0..3) {
                    val path = Path()
                    val baseHeight = h * (0.28f + layer * 0.14f)
                    val freq = 0.003f + layer * 0.001f
                    val phase = t * (1.2f + layer * 0.3f) + layer

                    path.moveTo(0f, h)
                    path.lineTo(0f, baseHeight)

                    var px = 0f
                    while (px <= w) {
                        val wave = sin(px * freq + phase) * 80f +
                                cos(px * freq * 0.5f + phase * 0.8f) * 45f +
                                sin((px - touchX * w) * 0.005f) * 30f
                        path.lineTo(px, baseHeight + wave)
                        px += 20f
                    }
                    path.lineTo(w, h)
                    path.close()

                    val colorStart = when (layer) {
                        0 -> Color(0x6610B981)
                        1 -> Color(0x5506B6D4)
                        2 -> Color(0x448B5CF6)
                        else -> Color(0x55059669)
                    }
                    val colorEnd = Color(0x00000000)

                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(colorStart, colorEnd),
                            startY = baseHeight - 100f,
                            endY = baseHeight + 250f
                        )
                    )
                }

                // Night landscape horizon silhouette
                val mountainPath = Path().apply {
                    moveTo(0f, h)
                    lineTo(0f, h * 0.82f)
                    lineTo(w * 0.25f, h * 0.74f)
                    lineTo(w * 0.45f, h * 0.79f)
                    lineTo(w * 0.7f, h * 0.71f)
                    lineTo(w, h * 0.85f)
                    lineTo(w, h)
                    close()
                }
                drawPath(mountainPath, Color(0xFF030712))
            }

            "AMOLED_MATRIX" -> {
                // Pitch black AMOLED background
                drawRect(Color(0xFF000000))

                val gridCols = 8
                val gridRows = 16
                val stepX = w / gridCols
                val stepY = h / gridRows

                // Draw pulsing digital grid nodes
                for (i in 0..gridCols) {
                    for (j in 0..gridRows) {
                        val nx = i * stepX
                        val ny = j * stepY
                        val distToTouch = kotlin.math.hypot(nx - touchX * w, ny - touchY * h)
                        val touchGlow = (1f - (distToTouch / (w * 0.7f)).coerceIn(0f, 1f))

                        val nodeAlpha = ((sin(t * 2f + (i * 3 + j)) * 0.4f + 0.6f) * 0.4f + touchGlow * 0.6f).coerceIn(0.1f, 1f)

                        // Connections
                        if (i < gridCols) {
                            drawLine(
                                color = Color(0xFF06B6D4).copy(alpha = nodeAlpha * 0.25f),
                                start = Offset(nx, ny),
                                end = Offset((i + 1) * stepX, ny),
                                strokeWidth = 1.2f
                            )
                        }
                        if (j < gridRows) {
                            drawLine(
                                color = Color(0xFF8B5CF6).copy(alpha = nodeAlpha * 0.25f),
                                start = Offset(nx, ny),
                                end = Offset(nx, (j + 1) * stepY),
                                strokeWidth = 1.2f
                            )
                        }

                        // Glowing node dot
                        val dotRadius = 2.5f + touchGlow * 4f
                        val dotColor = if (touchGlow > 0.4f) Color(0xFF38BDF8) else Color(0xFF8B5CF6)
                        drawCircle(
                            color = dotColor.copy(alpha = nodeAlpha),
                            radius = dotRadius,
                            center = Offset(nx, ny)
                        )
                    }
                }
            }

            "QUANTUM_PULSE" -> {
                // Deep dark purple-black void
                drawRect(Color(0xFF090314))

                val cx = touchX * w
                val cy = touchY * h

                // Concentric pulsing quantum rings
                for (r in 1..6) {
                    val ringProgress = ((t * 0.5f + r * 0.2f) % 1f)
                    val ringRadius = ringProgress * (w * 0.85f)
                    val ringAlpha = (1f - ringProgress).coerceIn(0f, 1f) * 0.7f

                    val ringColor = if (r % 2 == 0) Color(0xFFF43F5E) else Color(0xFF8B5CF6)

                    drawCircle(
                        color = ringColor.copy(alpha = ringAlpha),
                        radius = ringRadius,
                        center = Offset(cx, cy),
                        style = Stroke(width = 3.5f + (1f - ringProgress) * 4f)
                    )
                }

                // Core luminous pulsar
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF43F5E), Color(0xFF7C3AED), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = 120f
                    ),
                    radius = 120f,
                    center = Offset(cx, cy)
                )

                // Orbiting sparks
                for (p in 0..16) {
                    val angle = t * 1.8f + (p * 6.28f / 16f)
                    val dist = 140f + sin(t * 3f + p) * 30f
                    val sx = cx + cos(angle) * dist
                    val sy = cy + sin(angle) * dist
                    drawCircle(Color(0xFFFBBF24), radius = 4f, center = Offset(sx, sy))
                }
            }

            "FIREFLIES" -> {
                // Enchanted forest dark green/indigo backdrop
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF021B1A), Color(0xFF07271F), Color(0xFF041410))
                    )
                )

                for (p in particles) {
                    val fx = ((p.x + sin(t * p.speed + p.phase) * 0.1f + touchX * 0.05f) % 1f) * w
                    val fy = ((p.y - (t * p.speed * 0.2f) + cos(t * 0.5f + p.phase) * 0.08f) % 1f) * h
                    val glow = (sin(t * 3f + p.phase) * 0.4f + 0.6f).coerceIn(0.2f, 1f)

                    // Ambient halo
                    drawCircle(
                        color = Color(0x33FBBF24),
                        radius = p.radius * 6f * glow,
                        center = Offset(fx, fy)
                    )
                    // Firefly core
                    drawCircle(
                        color = Color(0xFFFEF08A).copy(alpha = glow),
                        radius = p.radius,
                        center = Offset(fx, fy)
                    )
                }
            }

            else -> {
                // Default: COSMIC_WARP (Deep Space & Stardust)
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF1E1035), Color(0xFF0B0D19), Color(0xFF05060A)),
                        center = Offset(touchX * w, touchY * h),
                        radius = w * 1.2f
                    )
                )

                val cx = touchX * w
                val cy = touchY * h

                // Swirling nebula clouds
                for (ring in 1..4) {
                    val ringAngle = t * 0.5f * (if (ring % 2 == 0) 1 else -1)
                    val radiusBase = w * (0.25f * ring)
                    val rx = cx + cos(ringAngle) * 40f
                    val ry = cy + sin(ringAngle) * 40f

                    val nebulaColor = when (ring) {
                        1 -> Color(0x44A78BFA)
                        2 -> Color(0x3306B6D4)
                        3 -> Color(0x22F43F5E)
                        else -> Color(0x22FBBF24)
                    }

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(nebulaColor, Color.Transparent),
                            center = Offset(rx, ry),
                            radius = radiusBase
                        ),
                        radius = radiusBase,
                        center = Offset(rx, ry)
                    )
                }

                // Stardust warp streams
                for (p in particles) {
                    val starTime = (t * p.speed + p.phase) % 6.28f
                    val starDist = ((starTime / 6.28f) * w * 0.9f)
                    val angle = p.phase * 3f + t * 0.2f
                    val sx = cx + cos(angle) * starDist
                    val sy = cy + sin(angle) * starDist
                    val starAlpha = (sin(starTime) * 0.5f + 0.5f).coerceIn(0.1f, 1f)

                    val starColor = if (p.radius > 3f) Color(0xFF38BDF8) else Color(0xFFEDE9FE)
                    drawCircle(
                        color = starColor.copy(alpha = starAlpha),
                        radius = p.radius,
                        center = Offset(sx, sy)
                    )
                }
            }
        }
    }
}

private data class SimulatedParticle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speed: Float,
    val phase: Float
)

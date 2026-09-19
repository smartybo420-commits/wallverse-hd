package com.example.service

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class WallVerseLiveWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return CosmicWallpaperEngine()
    }

    inner class CosmicWallpaperEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var isVisible = false
        private var width = 1080f
        private var height = 2400f
        private var touchX = 540f
        private var touchY = 1200f
        private var time = 0f

        private val backgroundPaint = Paint()
        private val starPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        private val nebulaPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        private val stars = List(120) {
            Star(
                x = Random.nextFloat() * 1080f,
                y = Random.nextFloat() * 2400f,
                radius = Random.nextFloat() * 3f + 1.2f,
                speed = Random.nextFloat() * 0.8f + 0.3f,
                color = when (Random.nextInt(4)) {
                    0 -> Color.rgb(139, 92, 246)  // Purple
                    1 -> Color.rgb(56, 189, 248)  // Cyan
                    2 -> Color.rgb(251, 191, 36)  // Gold
                    else -> Color.WHITE
                },
                alphaOffset = Random.nextFloat() * 6.28f
            )
        }

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                if (isVisible) {
                    handler.postDelayed(this, 33) // ~30 FPS for battery saving
                }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            isVisible = visible
            if (visible) {
                handler.post(drawRunnable)
            } else {
                handler.removeCallbacks(drawRunnable)
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
            super.onSurfaceChanged(holder, format, w, h)
            width = w.toFloat().coerceAtLeast(100f)
            height = h.toFloat().coerceAtLeast(100f)
            touchX = width / 2f
            touchY = height / 2f
        }

        override fun onTouchEvent(event: MotionEvent) {
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                touchX = event.x
                touchY = event.y
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(drawRunnable)
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    time += 0.03f

                    // Deep space background gradient
                    canvas.drawColor(Color.rgb(10, 11, 20))

                    // Dynamic nebula glow around touch / center
                    val nebulaGradient = RadialGradient(
                        touchX + cos(time) * 60f,
                        touchY + sin(time * 0.7f) * 60f,
                        width * 0.65f,
                        intArrayOf(
                            Color.argb(80, 139, 92, 246),
                            Color.argb(40, 6, 182, 212),
                            Color.TRANSPARENT
                        ),
                        floatArrayOf(0f, 0.5f, 1f),
                        Shader.TileMode.CLAMP
                    )
                    nebulaPaint.shader = nebulaGradient
                    canvas.drawRect(0f, 0f, width, height, nebulaPaint)

                    // Draw moving cosmic stars
                    for (star in stars) {
                        star.y += star.speed
                        if (star.y > height) {
                            star.y = 0f
                            star.x = Random.nextFloat() * width
                        }

                        // Twinkle effect
                        val alphaFactor = (sin(time * 2f + star.alphaOffset) * 0.35f + 0.65f)
                        starPaint.color = star.color
                        starPaint.alpha = (alphaFactor * 255).toInt().coerceIn(40, 255)

                        canvas.drawCircle(star.x, star.y, star.radius, starPaint)
                    }
                }
            } catch (e: Exception) {
                // Ignore drawing exceptions during surface tear-down
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        // Ignore
                    }
                }
            }
        }
    }

    private data class Star(
        var x: Float,
        var y: Float,
        val radius: Float,
        val speed: Float,
        val color: Int,
        val alphaOffset: Float
    )
}

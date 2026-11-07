package com.example.mediaplayer.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.mediaplayer.R
import kotlin.random.Random

class WaveformView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = context.getColor(R.color.neon_cyan)
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val bars = 30
    private var amplitudes = FloatArray(bars) { 10f }
    private var isPlaying = false

    fun setPlaying(playing: Boolean) {
        isPlaying = playing
        if (playing) invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val widthBetweenBars = width.toFloat() / bars
        
        for (i in 0 until bars) {
            if (isPlaying) {
                amplitudes[i] = Random.nextFloat() * height.toFloat() * 0.8f
            } else {
                amplitudes[i] = 10f
            }
            
            val x = i * widthBetweenBars + widthBetweenBars / 2
            val startY = height / 2 - amplitudes[i] / 2
            val stopY = height / 2 + amplitudes[i] / 2
            canvas.drawLine(x, startY, x, stopY, paint)
        }

        if (isPlaying) {
            postInvalidateDelayed(100)
        }
    }
}

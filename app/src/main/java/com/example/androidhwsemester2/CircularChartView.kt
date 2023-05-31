package com.example.androidhwsemester2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CircularChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private val startAngle = -90f
    private val strokeWidth = 40f
    private val chartRect = RectF()

    var data: List<Pair<Int, Int>> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val chartPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = 24f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width.toFloat() / 2
        val centerY = height.toFloat() / 2
        val radius = (min(width, height) / 2) - (strokeWidth / 2)

        chartRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        var currentAngle = startAngle

        for ((color, percent) in data) {
            val sweepAngle = (360f * percent) / 100f

            chartPaint.color = color
            canvas.drawArc(chartRect, currentAngle, sweepAngle, true, chartPaint)

            val textRadius = radius * 0.5f
            val textAngle = currentAngle + (sweepAngle / 2)
            val textX =
                centerX + (textRadius * cos(Math.toRadians(textAngle.toDouble()))).toFloat()
            val textY =
                centerY + (textRadius * sin(Math.toRadians(textAngle.toDouble()))).toFloat()

            val text = "$percent%"
            canvas.drawText(text, textX, textY, textPaint)

            currentAngle += sweepAngle
        }
    }
}
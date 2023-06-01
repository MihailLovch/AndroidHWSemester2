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

    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f

    var data: List<Pair<Int, Int>> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var segmentsData = mutableListOf<Segment>()

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

    private fun calculateSegments() {
        segmentsData = mutableListOf()

        var currentAngle = startAngle

        for ((color, percent) in data) {

            val sweepAngle = (360f * percent) / 100f

            val textRadius = radius * 0.5f
            val textAngle = currentAngle + (sweepAngle / 2)
            val textX =
                centerX + (textRadius * cos(Math.toRadians(textAngle.toDouble()))).toFloat()
            val textY =
                centerY + (textRadius * sin(Math.toRadians(textAngle.toDouble()))).toFloat()
            val text = "$percent%"

            segmentsData.add(
                Segment(
                    startAngle = currentAngle,
                    endAngle = sweepAngle,
                    color = color,
                    text = text,
                    textX = textX,
                    textY = textY
                )
            )

            currentAngle += sweepAngle
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        centerX = width.toFloat() / 2
        centerY = height.toFloat() / 2
        radius = (min(width, height) / 2) - (strokeWidth / 2)

        chartRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        setMeasuredDimension(width,height)

        calculateSegments()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (segment in segmentsData) {
            with(segment) {
                chartPaint.color = color
                canvas.drawArc(chartRect, startAngle, endAngle, true, chartPaint)
                canvas.drawText(text, textX, textY, textPaint)
            }
        }
    }
}

private data class Segment(
    val startAngle: Float,
    val endAngle: Float,
    val color: Int,
    val text: String,
    val textX: Float,
    val textY: Float,
)
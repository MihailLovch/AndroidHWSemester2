package com.example.androidhwsemester2

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CircularChartView>(R.id.chart_ch).data = listOf(
            Pair(Color.RED, 25),
            Pair(Color.BLUE, 8),
            Pair(Color.MAGENTA, 20),
            Pair(Color.GREEN, 29),
            Pair(Color.YELLOW, 16),
            Pair(Color.LTGRAY,100-25-8-20-29-16)
        )
    }
}
package com.adrosonic.adroscanner.util

import android.graphics.*
import android.util.Log

class RectGraphic internal constructor(overlay: GraphicOverlay): GraphicOverlay.Graphic(overlay) {

    private val rectPaint: Paint = Paint()

    init{
        rectPaint.color = Color.BLACK
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = STROKE_WIDTH
        rectPaint.pathEffect = DashPathEffect(floatArrayOf(40f,40f),0f)
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }
    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas:Canvas) {
        Log.d(TAG, "on draw text graphic")
        // Draws the bounding box around the TextBlock.
        val boundary = canvas.width.toFloat()/6
        Log.d(TAG,boundary.toString())
        val rect = RectF(boundary, boundary, canvas.width.toFloat()-boundary, canvas.height.toFloat()-boundary)
        canvas.drawRect(rect, rectPaint)
    }
    companion object {
        private val TAG = "TextGraphic"
        private val STROKE_WIDTH = 4.0f
    }
}
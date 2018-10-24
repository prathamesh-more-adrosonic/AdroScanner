package com.adrosonic.adroscanner.modules.camera

import android.graphics.*
import android.util.Log
import com.google.firebase.ml.vision.text.FirebaseVisionText

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

        val rect = RectF(BOUNDARY, BOUNDARY, canvas.width.toFloat() - BOUNDARY, canvas.height.toFloat() - BOUNDARY)
        canvas.drawRect(rect, rectPaint)
    }
    companion object {
        private val TAG = "TextGraphic"
        private val STROKE_WIDTH = 4.0f
        private var BOUNDARY = 300f
    }
}
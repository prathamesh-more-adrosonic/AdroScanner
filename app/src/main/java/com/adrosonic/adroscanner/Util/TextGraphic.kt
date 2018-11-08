package com.adrosonic.adroscanner.Util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.google.firebase.ml.vision.text.FirebaseVisionText

class TextGraphic internal constructor(overlay: GraphicOverlay, private val line: FirebaseVisionText.Line): GraphicOverlay.Graphic(overlay) {

    private val rectPaint: Paint = Paint()

    init{
        rectPaint.color = Color.argb(50,255,213,0)
        rectPaint.style = Paint.Style.FILL
        rectPaint.strokeWidth = STROKE_WIDTH
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }
    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas:Canvas) {
        Log.d(TAG, "on draw text graphic")
        // Draws the bounding box around the TextBlock.
        val rect = RectF(line.boundingBox)
        canvas.drawRect(rect, rectPaint)
    }
    companion object {
        private val TAG = "TextGraphic"
        private val STROKE_WIDTH = 4.0f
    }
}
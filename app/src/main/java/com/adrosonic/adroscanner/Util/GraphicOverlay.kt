package com.adrosonic.adroscanner.Util

import android.content.Context
import android.graphics.Canvas
import android.hardware.camera2.CameraCharacteristics
import android.os.Build
import android.util.AttributeSet
import android.view.View

/**
 * A view which renders a series of custom graphics to be overlayed on top of an associated preview
 * (i.e., the camera preview). The creator can add graphics objects, update the objects, and remove
 * them, triggering the appropriate drawing and invalidation within the view.
 * <p>
 * <p>Supports scaling and mirroring of the graphics relative the camera's preview properties. The
 * idea is that detection items are expressed in terms of a preview size, but need to be scaled up
 * to the full view size, and also mirrored in the case of the front-facing camera.
 * <p>
 * <p>Associated {@link Graphic} items should use the following methods to convert to view
 * coordinates for the graphics that are drawn:
 * <p>
 * <ol>
 * <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
 * supplied value from the preview scale to the view scale.
 * <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
 * coordinate from the preview's coordinate system to the view coordinate system.
 * </ol>
 */
class GraphicOverlay(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var lock = Any()
    private var previewWidth:Int = 0
    private var widthScaleFactor = 1.0f
    private var previewHeight:Int = 0
    private var heightScaleFactor = 1.0f
    private var facing = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        CameraCharacteristics.LENS_FACING_BACK
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }
    private var graphics = HashSet<Graphic>()
    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the {@link Graphic#draw(Canvas)} method to define the graphics element. Add
     * instances to the overlay using {@link GraphicOverlay#add(Graphic)}.
     */
    abstract class Graphic(private val overlay: GraphicOverlay) {
        /**
         * Returns the application context of the app.
         */
        val applicationContext:Context
            get() {
                return overlay.context.applicationContext
            }

        /**
         * Draw the graphic on the supplied canvas. Drawing should use the following methods to convert
         * to view coordinates for the graphics that are drawn:
         * <p>
         * <ol>
         * <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
         * supplied value from the preview scale to the view scale.
         * <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
         * coordinate from the preview's coordinate system to the view coordinate system.
         * </ol>
         *
         * @param canvas drawing canvas
         */
        abstract fun draw(canvas: Canvas)
        /**
         * Adjusts a horizontal value of the supplied value from the preview scale to the view scale.
         */
        fun scaleX(horizontal:Float):Float {
            return horizontal * overlay.widthScaleFactor
        }
        /**
         * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
         */
        fun scaleY(vertical:Float):Float {
            return vertical * overlay.heightScaleFactor
        }
        /**
         * Adjusts the x coordinate from the preview's coordinate system to the view coordinate system.
         */
        fun translateX(x:Float):Float {
            if (overlay.facing == CameraCharacteristics.LENS_FACING_FRONT)
            {
                return overlay.width - scaleX(x)
            }
            else
            {
                return scaleX(x)
            }
        }
        /**
         * Adjusts the y coordinate from the preview's coordinate system to the view coordinate system.
         */
        fun translateY(y:Float):Float {
            return scaleY(y)
        }
        fun postInvalidate() {
            overlay.postInvalidate()
        }
    }
    /**
     * Removes all graphics from the overlay.
     */
    fun clear() {
        synchronized (lock) {
            graphics.clear()
        }
        postInvalidate()
    }
    /**
     * Adds a graphic to the overlay.
     */
    fun add(graphic: Graphic) {
        synchronized (lock) {
            graphics.add(graphic)
        }
        postInvalidate()
    }
    /**
     * Removes a graphic from the overlay.
     */
    fun remove(graphic: Graphic) {
        synchronized (lock) {
            graphics.remove(graphic)
        }
        postInvalidate()
    }
    /**
     * Sets the camera attributes for size and facing direction, which informs how to transform image
     * coordinates later.
     */
    fun setCameraInfo(previewWidth:Int, previewHeight:Int, facing:Int) {
        synchronized (lock) {
            this.previewWidth = previewWidth
            this.previewHeight = previewHeight
            this.facing = facing
        }
        postInvalidate()
    }

    /**
     * Draws the overlay with its associated graphic objects.
     */
    override fun onDraw(canvas:Canvas) {
        super.onDraw(canvas)
        synchronized (lock) {
            if ((previewWidth != 0) && (previewHeight != 0))
            {
                widthScaleFactor = canvas.width / previewWidth.toFloat()
                heightScaleFactor = canvas.height / previewHeight.toFloat()
            }
            for (graphic in graphics)
            {
                graphic.draw(canvas)
            }
        }
    }
}
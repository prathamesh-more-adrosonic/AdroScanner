package com.adrosonic.adroscanner.modules.camera

import android.content.Context
import android.hardware.Camera
import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import java.io.IOException

/** A basic Camera preview class */
class CameraPreview(
        context: Context,
        private var mCamera: Camera ?= null
) : SurfaceView(context), SurfaceHolder.Callback {

    private val mHolder: SurfaceHolder = holder.apply {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        addCallback(this@CameraPreview)
        // deprecated setting, but required on Android versions prior to 3.0
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    var supportedPreviewSizes: MutableList<Camera.Size> ?= mCamera?.parameters?.supportedPreviewSizes
    var previewSize: Camera.Size ?= null

    override fun surfaceCreated(holder: SurfaceHolder) {

        // The Surface has been created, now tell the camera where to draw the preview.
//        mCamera?.apply {
//            try {
//                setPreviewDisplay(holder)
//                startPreview()
//            } catch (e: IOException) {
//                Log.d("setting preview",e.message)
//            }
//        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            mCamera?.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        mCamera?.apply {
            try {
                parameters?.also {params ->
                    previewSize?.let {
                        params.setPreviewSize(it.width,it.height)
                    }
                    requestLayout()
                    parameters = params
                }
                val rotation: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    CameraActivity.getRotationCompensation("0", context as CameraActivity, context).toInt()
                } else 90
                setDisplayOrientation(rotation)
                setPreviewDisplay(mHolder)
                startPreview()
            } catch (e: Exception) {
                Log.d("starting preview", e.message)
            }
        }
    }

    fun setCamera(camera: Camera?){
        if (mCamera == camera)
            return

        mCamera?.stopPreview()
//        mCamera = camera
//
//        mCamera?.apply {
//            supportedPreviewSizes = parameters.supportedPreviewSizes
//            previewSize = getOptimalPreviewSize(supportedPreviewSizes,resources.displayMetrics.widthPixels,resources.displayMetrics.heightPixels)
//            parameters?.also {params ->
//                previewSize?.let {
//                    params.setPreviewSize(it.width, it.height)
//                }
//                requestLayout()
//                parameters = params
//            }
//            requestLayout()
//
//            try {
//                setPreviewDisplay(holder)
//            }catch (e: IOException){
//                Log.e("Set Camera",e.message)
//            }
//            startPreview()
//        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = View.resolveSize(suggestedMinimumHeight,widthMeasureSpec)
        val width = View.resolveSize(suggestedMinimumWidth,heightMeasureSpec)

        if (supportedPreviewSizes != null){
            previewSize = getOptimalPreviewSize(supportedPreviewSizes,width,height)
        }
        val ratio = previewSize?.let {camSize -> camSize.width.toFloat()/camSize.height } ?: 1f
        setMeasuredDimension(height,(height*ratio).toInt())
    }

    fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int,h: Int): Camera.Size?{
        val targetRatio = w.toDouble()/h
        Log.i("Target Ratio",targetRatio.toString())
        var optimalSize: Camera.Size ?= null
        val currentRatio = mCamera?.parameters?.previewSize?.let { it.width.toDouble()/it.height }
        var minDiff: Double = if (currentRatio != null) Math.abs(targetRatio - currentRatio) else Double.MAX_VALUE
        sizes?.forEach { size ->
            val ratio = size.width.toDouble()/size.height
            if (Math.abs(ratio - targetRatio) < minDiff){
                optimalSize = size
                minDiff = Math.abs(ratio - targetRatio)
            }
        }
        if (optimalSize == null){
            optimalSize = mCamera?.parameters?.previewSize
        }
        return optimalSize
    }

}
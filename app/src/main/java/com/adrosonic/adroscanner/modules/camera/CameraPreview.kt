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

    var supportedPreviewSizes: MutableList<Camera.Size> ?= null

    override fun surfaceCreated(holder: SurfaceHolder) {

        // The Surface has been created, now tell the camera where to draw the preview.
        mCamera?.apply {
            try {
                setPreviewDisplay(holder)
                startPreview()
            } catch (e: IOException) {
                Log.d("setting preview",e.message)
            }
        }
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
//        val parameters = mCamera.parameters
//        mCamera.parameters.setPreviewSize(1088,1088)
//        mCamera.parameters = parameters
        mCamera?.apply {
            try {
//                parameters?.also {
//                    it.setPreviewSize(1088, 1088)
//                    requestLayout()
//                    parameters = it
//                }
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

        stopPreviewAndFreeCamera()

        mCamera = camera

        mCamera?.apply {
            supportedPreviewSizes = parameters.supportedPreviewSizes
            requestLayout()

            try {
                setPreviewDisplay(holder)
            }catch (e: IOException){
                Log.e("Set Camera",e.message)
            }
            startPreview()
        }

    }

    private fun stopPreviewAndFreeCamera() {
        mCamera?.apply {
            // Call stopPreview() to stop updating the preview surface.
            stopPreview()

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            release()

            mCamera = null
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = View.resolveSize(suggestedMinimumHeight,widthMeasureSpec)
        val width = View.resolveSize(suggestedMinimumWidth,heightMeasureSpec)


        val ratio = mCamera?.let {
            it.parameters.previewSize.width.toDouble()/it.parameters.previewSize.height
        } ?: 1.toDouble()

//        setMeasuredDimension(mCamera.parameters.previewSize.height,mCamera.parameters.previewSize.width)
        setMeasuredDimension(height,(height*ratio).toInt())
//        supportedPreviewSize.forEach {
//            val ratio = (it.width/it.height).toDouble()
//            if (Math.abs(ratio - targetRatio) > 0.1)
//            return@forEach
//            if (Math.abs(it.height - height) < Double.MAX_VALUE){
//                val optimalRatio = (it.width/it.height).toFloat()
//                setMeasuredDimension(it.height,(it.height*optimalRatio).toInt())
//            }
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
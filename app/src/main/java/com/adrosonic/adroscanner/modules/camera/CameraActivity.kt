package com.adrosonic.adroscanner.modules.camera

import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_camera.*
import android.util.Log
import kotlinx.android.synthetic.main.activity_camera.view.*
import android.util.SparseIntArray
import android.view.Surface
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraAccessException
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicConvolve3x3
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import com.adrosonic.adroscanner.R
import com.adrosonic.adroscanner.modules.result.ResultActivity
import com.adrosonic.adroscanner.entity.UserEntity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.adrosonic.adroscanner.modules.camera.CameraPreview

class CameraActivity : AppCompatActivity() {

    private var camera: Camera? = null
    private var cameraPreview: CameraPreview? = null

    companion object {
        var user = UserEntity()

        /**
         * Get the angle by which an image must be rotated given the device's current
         * orientation.
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Throws(CameraAccessException::class)
        fun getRotationCompensation(cameraId: String, activity: Activity, context: Context): Float {

            val ORIENTATIONS = SparseIntArray()

            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)

            // Get the device's current rotation relative to its "native" orientation.
            // Then, from the ORIENTATIONS table, look up the angle the image must be
            // rotated to compensate for the device's rotation.
            val deviceRotation = activity.windowManager.defaultDisplay.rotation
            var rotationCompensation = ORIENTATIONS.get(deviceRotation)

            // On most devices, the sensor orientation is 90 degrees, but for some
            // devices it is 270 degrees. For devices with a sensor orientation of
            // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val sensorOrientation = cameraManager
                    .getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.SENSOR_ORIENTATION)!!
            rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360

            // Return the corresponding FirebaseVisionImageMetadata rotation value.
//        val result: Int
//        when (rotationCompensation) {
//            0 -> result = FirebaseVisionImageMetadata.ROTATION_0
//            90 -> result = FirebaseVisionImageMetadata.ROTATION_90
//            180 -> result = FirebaseVisionImageMetadata.ROTATION_180
//            270 -> result = FirebaseVisionImageMetadata.ROTATION_270
//            else -> {
//                result = FirebaseVisionImageMetadata.ROTATION_0
//                Log.e("Bad rotation value",rotationCompensation.toString())
//            }
//        }
//        return result
            return rotationCompensation.toFloat()
        }

        var rotation = 0f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        camera = getCameraInstance()
        cameraPreview = camera?.let {
            // Create our Preview view
            CameraPreview(this, it)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        rotation = getRotationCompensation("0",this,this)
        // Set the Preview view as the content of our activity.
        cameraPreview?.also {
            camera_preview.addView(it)
        }

        val rectGraphic = RectGraphic(graphicOverlay)
        graphicOverlay.add(rectGraphic)
        // Capturing the images
        val picture = PictureCallback { data, _ ->
            Observable.fromCallable{performImageProcessing(data)}
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{bitmap ->
//                        val user = UserEntity()
                        val nameList = arrayListOf<String>()
                        camera_preview.visibility = View.GONE
                        image_view.visibility = View.VISIBLE
                        user.bitmap = bitmap
//                        image_view.setImageBitmap(bitmap.rotate(-rotation))
                        image_view.setImageBitmap(bitmap)
                        val image = FirebaseVisionImage.fromBitmap(bitmap)
                        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
                        textRecognizer.processImage(image)
                                .addOnSuccessListener{fbText ->
                                    Log.i("Text",fbText.text)
                                    fbText.let {
                                        processTextRecognitionResult(it)
                                        fbText.textBlocks.forEach changeBlock@{block ->
                                            val blockText = block.text
                                            if (isNameandDesignation(blockText))
                                                block.lines.forEach { line ->
                                                    if (!line.text.contains(".co") && !line.text.contains("w."))
                                                        nameList.add(line.text)
                                                    else
                                                        user.website += line.text
                                                }
                                            else
                                                block.lines.forEach { line ->
                                                    val lineText = line.text

                                                    when {
                                                        lineText.contains(".co") && lineText.contains("w.") -> user.website = lineText
                                                        isPinCode(lineText) -> {
                                                            user.address += lineText
                                                        }
                                                        lineText.contains("@") -> user.email += lineText
                                                        isPhoneNumber(lineText) -> user.phoneNumber += lineText
                                                    }

                                                    Log.i("Lines", lineText)
                                                }
                                            Log.i("Blocks", blockText)
                                        }
                                        nameList.trimToSize()
                                        if (nameList.isNotEmpty())
                                            when (nameList.size){
                                                1 -> user.name = nameList[0]
                                                2 -> {
                                                    user.name = nameList[0]
                                                    user.jobTitle = nameList[1]
                                                }
                                                3 -> {
                                                    user.name = nameList[1]
                                                    user.jobTitle = nameList[2]
                                                    user.company = nameList[0]
                                                }
                                                else -> {
                                                    user.name = nameList.toString()
                                                }
                                            }
                                    }
                                }
                    }
//            val bitmap = getScaledBitmap(data,camera_preview.height,camera_preview.width)
//            val sharp: FloatArray =
//                    floatArrayOf(0f, -1f, 0f,
//                    -1f, 5f, -1f,
//                    0f, -1f, 0f)
//            bitmap = doSharp(bitmap,sharp)
//            camera_preview.visibility = View.GONE
//            image_view.visibility = View.VISIBLE
//            val rotation = getRotationCompensation("0",this,this)
//            image_view.setImageBitmap(bitmap.rotate(rotation))
        }

        camera_preview.setOnClickListener{
            camera?.autoFocus { _, _ ->  }
        }

        control.picture.setOnClickListener {
            camera?.autoFocus { success, camera ->
               if (success) {
                   camera?.takePicture(null, null, picture)
                   control.visibility = View.GONE
                   results_btn.visibility = View.VISIBLE
               }
            }
        }

        results_btn.setOnClickListener {
            val resultIntent = Intent(this, ResultActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("user",user)
            resultIntent.putExtras(bundle)
            startActivity(resultIntent)
        }
    }

    private fun performImageProcessing(data: ByteArray): Bitmap{
//        val bitmap = getScaledBitmap(data,camera_preview.height,camera_preview.width)
        val bitmap = getScaledBitmap(data,camera_preview.width,(camera_preview.width*camera_preview.width)/camera_preview.height)
        return bitmap.rotate(rotation*2)
    }

    private fun isPinCode(pin: String): Boolean{
        val pinText = pin.filter { it.isDigit() }
        return pinText.isNotEmpty() && pinText.length < 7
    }

    private fun isNameandDesignation(text: String): Boolean{
        return text.isNotEmpty() && !text.contains("@") && !text.any { it.isDigit() && !text.contains(".co") && !text.contains("w.")}
    }

    private fun isPhoneNumber(phone: String): Boolean{

        val phoneText = phone.toLowerCase()
                .replace(".","")
                .replace(":","")
                .replace(",","")
                .replace("mob","")
                .replace("mobile","")
                .replace("tel","")
                .replace("phone","")
                .replace("cell","")
                .replace("c","")

        phoneText.replace("o","0")
                .replace("t","1")

        return phoneText.matches(Regex("(\\+?)(\\([0-9OoA]{3}\\))?([\\s-0-9OoA]+)\$")) && phoneText.length > 6
    }

    private  fun doSharp(original: Bitmap, radius : FloatArray): Bitmap{
        val bitmap = Bitmap.createBitmap(original.width,original.height,Bitmap.Config.ARGB_8888)
        val renderScript = RenderScript.create(this)
        val allocIn = Allocation.createFromBitmap(renderScript,original)
        val allocOut = Allocation.createFromBitmap(renderScript, bitmap)
        val convolution = ScriptIntrinsicConvolve3x3.create(renderScript, Element.A_8(renderScript))
        convolution.setInput(allocIn)
        convolution.setCoefficients(radius)
        convolution.forEach(allocOut)
        allocOut.copyTo(bitmap)
        renderScript.destroy()
        return bitmap
    }

    private fun isEmailId(email: String): Boolean{
        return email.matches(Regex("([\\sa-zA-Z0-9_.]+)@([a-zA-Z0-9_.]+)\\.([a-zA-Z]{2,5})$"))
    }
    private fun processTextRecognitionResult(text: FirebaseVisionText){
        val blockTexts = text.textBlocks
        if (blockTexts.isEmpty()) {
            Toast.makeText(this, "No text detected", Toast.LENGTH_SHORT).show()
            return
        }
//        graphicOverlay.rotation = -rotation
        graphicOverlay.clear()
        blockTexts.forEach { block ->
            val lineTexts = block.lines
            lineTexts.forEach {line ->
                line.boundingBox?.offset(0,(camera_preview.height - ((camera_preview.width*camera_preview.width)/camera_preview.height))/2)
                val textGraphic = TextGraphic(graphicOverlay, line)
                graphicOverlay.add(textGraphic)
            }
        }
    }

    private fun getCameraInstance(): Camera?{
        return try {
            Camera.open(0)
        }catch (e: Exception){
            null
        }
    }

    private fun getScaledBitmap(originalImage: ByteArray, newWidth: Int,newHeight: Int): Bitmap{

        val bitmapImage = BitmapFactory.decodeByteArray(originalImage, 0, originalImage.size)

        val mutableBitmapImage = Bitmap.createScaledBitmap(bitmapImage,newWidth,newHeight,false)
        bitmapImage.recycle()
        return mutableBitmapImage
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}

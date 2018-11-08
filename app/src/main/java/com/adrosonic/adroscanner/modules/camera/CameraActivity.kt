package com.adrosonic.adroscanner.modules.camera

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_camera.*
import android.util.Log
import kotlinx.android.synthetic.main.activity_camera.view.*
import android.util.SparseIntArray
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
import android.os.Environment
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicConvolve3x3
import android.support.annotation.RequiresApi
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.adrosonic.adroscanner.App
import com.adrosonic.adroscanner.R
import com.adrosonic.adroscanner.Util.RectGraphic
import com.adrosonic.adroscanner.modules.result.ResultActivity
import com.adrosonic.adroscanner.entity.UserEntity
import com.adrosonic.adroscanner.modules.login.MainActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private var camera: Camera? = null
    private var cameraPreview: CameraPreview? = null
    private var orientationEventListener: OrientationEventListener? = null

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
            return rotationCompensation.toFloat()
        }

        var rotation = 0f
        var currentPhotoPath: String ?= ""
        private var RESULT_LOAD_IMAGE: Int = 1
        var align = "portrait"
        var angle = 0f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        user = UserEntity()

        orientationEventListener = object : OrientationEventListener(this){
            override fun onOrientationChanged(orientation: Int) {
                when (orientation) {
                    in 45..134 -> {
                        if (align != "landscape")
                        {
                            align = "landscape"
                            spin(-90f)
                            Log.i("Orientation", "Landscape $orientation")
                        }
                    }
                    in 135..224 -> {

                    }
                    in 225..314 ->{
                        if (align != "reverse landscape")
                        {
                            align = "reverse landscape"
                            spin(90f)
                            Log.i("Orientation","Reverse Landscape $orientation")
                        }
                    }
                    in 315..360 -> {
                        if (align != "portrait")
                        {
                            align = "portrait"
                            spin(0f)
                            Log.i("Orientation","Portrait $orientation")
                        }
                    }
                    in 0..45 -> {
                        if (align != "portrait")
                        {
                            align = "portrait"
                            spin(0f)
                            Log.i("Orientation","Portrait $orientation")
                        }
                    }
                    else -> {
                        Log.i("Orientation","Invalid $orientation")
                    }
                }
            }

        }

        // Capturing the images
        val picture = PictureCallback { data, _ ->
            Observable.fromCallable{performImageProcessing(data)}
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{bitmap ->
                        user.imagePath = currentPhotoPath
                        val image = FirebaseVisionImage.fromBitmap(bitmap)
                        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
                        textRecognizer.processImage(image)
                                .addOnSuccessListener {
                                    processTextRecognitionResult(it)
                                    val resultIntent = Intent(this, ResultActivity::class.java)
                                    val bundle = Bundle()
                                    bundle.putParcelable("user",user)
                                    resultIntent.putExtras(bundle)
                                    startActivity(resultIntent)
                                }
                    }
        }

        camera_preview.setOnClickListener{
            camera?.autoFocus { _, _ ->  }
        }

        control.library.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE)
        }

        control.picture.setOnClickListener {
            camera?.autoFocus { success, camera ->
               if (success) {
                   camera?.takePicture(null, null, picture)
                   control.visibility = View.INVISIBLE
               }else{
                   Toast.makeText(this,"Auto Focus Failed",Toast.LENGTH_SHORT).show()
               }
            }
        }
    }

    override fun onStart() {
        super.onStart()
//        releaseCameraAndPreview()
        camera = getCameraInstance()
//        camera?.apply {
//            this.parameters?.also {params ->
//                optimalSize = (CameraPreview::getOptimalPreviewSize)(CameraPreview(
//                        this@CameraActivity,
//                        this)
//                        ,params.supportedPictureSizes
//                        ,camera_preview.width
//                        ,camera_preview.height)
//                optimalSize?.let {
//                    params.setPreviewSize(it.width, it.height)
//                    aspectRatio = it.width.toDouble()/it.height
//                }
//                parameters = params
//            }
//        }
        cameraPreview = camera?.let {
            // Create our Preview view
            CameraPreview(this, it)
        }

        rotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getRotationCompensation("0",this,this)
        }else 90f

        // Set the Preview view as the content of our activity.
        cameraPreview?.also {
            camera_preview.addView(it)
        }

        val rectGraphic = RectGraphic(graphicOverlay)
        graphicOverlay.add(rectGraphic)
        // Capturing the images

    }

    override fun onResume() {
        super.onResume()
        orientationEventListener?.enable()
    }

    override fun onPause() {
        orientationEventListener?.disable()
        camera?.release()
        camera = null
        super.onPause()
    }

    private fun performImageProcessing(data: ByteArray): Bitmap{
        val imageFile = createImageFile()
        if (imageFile.exists()) imageFile.delete()
        val fos = FileOutputStream(imageFile.path)
        val bitmap = BitmapFactory.decodeByteArray(data,0,data.size)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
        fos.flush()
        fos.close()
        user.rotation = rotation - angle
        return bitmap.rotate(rotation - angle)
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

    private fun doSharp(original: Bitmap, radius : FloatArray): Bitmap{
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

    private fun processTextRecognitionResult(fbText: FirebaseVisionText){
        val nameList = arrayListOf<String>()
        val numberList = arrayListOf<String>()
        Log.i("Text",fbText.text)
        fbText.let {
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
                            isPhoneNumber(lineText) -> numberList.add(lineText)
                        }

                        Log.i("Lines", lineText)
                    }
                Log.i("Blocks", blockText)
            }
            nameList.trimToSize()
            numberList.trimToSize()
            if (numberList.isNotEmpty())
                when(numberList.size){
                    2 -> {
                        user.phoneNumber = numberList[0]
                        user.phoneNumberAlt = numberList[1]
                    }
                    else -> user.phoneNumber = numberList[0]
                }
            if (nameList.isNotEmpty())
                when (nameList.size){
                    1 -> user.name = nameList[0]
                    2 -> {
                        user.name = nameList[0]
                        user.jobTitle = nameList[1]
                    }
                    3 -> {
                        user.company = nameList[0]
                        user.name = nameList[1]
                        user.jobTitle = nameList[2]
                    }
                    else -> {
                        user.name = nameList.toString()
                    }
                }
        }
    }

    private fun getCameraInstance(): Camera?{
        return try {
//            releaseCameraAndPreview()
            Camera.open(0)
        }catch (e: Exception){
            Log.e("Camera Instantiation",e.message)
            null
        }
    }

    private fun spin(rotationAngle: Float){
        // 1
        val valueAnimator = ValueAnimator.ofFloat(angle, rotationAngle)
        angle = rotationAngle
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            // 2
            control.library.rotation = value
        }

        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.start()

    }

    private fun getScaledBitmap(originalImage: ByteArray, newWidth: Int,newHeight: Int): Bitmap{
        Log.i("Image Size","${image_view.width}/${image_view.height}")
        Log.i("Preview Size","${camera_preview.width}/${camera_preview.height}")
        val bitmapImage = BitmapFactory.decodeByteArray(originalImage, 0, originalImage.size)
//        camera?.parameters?.previewSize?.let {
//            val aspectRatio = it.width.toDouble()/it.height
//            val bitmapScaled = Bitmap.createScaledBitmap(bitmapImage,it.width,it.height,false)
//            return if (newWidth* aspectRatio > newHeight) {
//                val bitmapCrop = Bitmap.createBitmap(bitmapScaled, 0, 0, (it.height * (newWidth.toDouble() / newHeight)).toInt(), it.height)
//                bitmapImage.recycle()
//                bitmapScaled.recycle()
//                Bitmap.createScaledBitmap(bitmapCrop, newHeight, newWidth, false)
//            } else{
//                val bitmapCrop = Bitmap.createBitmap(bitmapScaled, 0, 0, it.width, (it.width * (newHeight.toDouble() / newWidth)).toInt())
//                bitmapImage.recycle()
//                bitmapScaled.recycle()
//                Bitmap.createScaledBitmap(bitmapCrop, newHeight, newWidth, false)
//            }
//        }
//        Log.i("Camera Size","${camera?.parameters?.previewSize?.width}/${camera?.parameters?.previewSize?.height}")
//        Log.i("Picture Size","${camera?.parameters?.pictureSize?.width}/${camera?.parameters?.pictureSize?.height}")
//        Log.i("BitMap Size","${bitmapImage.width}/${bitmapImage.height}")
//        val pictureRatio = bitmapImage.width.toDouble()/bitmapImage.height
//        if (newWidth* aspectRatio > newHeight)
//            val bitmapCrop = Bitmap.createBitmap(bitmapImage,0,0,,bitmapImage.height)
//        val camWidth = bitmapImage.width
//        val camHeight = bitmapImage.height
//        val ratio = camWidth.toDouble()/camHeight
//        val offset = (newWidth*ratio) - newHeight
//        return if (offset > 0){
//            val newOffset = (offset*camWidth)/(newWidth*ratio)
//            val bitmapCrop = Bitmap.createBitmap(bitmapImage,0,0,camWidth-newOffset.toInt(),camHeight)
//            val mutableBitmapImage = Bitmap.createScaledBitmap(bitmapCrop,newHeight,newWidth,false)
//            bitmapImage.recycle()
//            mutableBitmapImage
//        }else{
            return Bitmap.createScaledBitmap(bitmapImage,newHeight,newWidth,false)
//        }
//        bitmapCrop.recycle()

//        return bitmapCrop
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_landing,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.menuLogout -> {
                try {
                    (application.applicationContext as App).auth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }catch (e: Exception){
                    Log.e("Logout Landing",e.message)
                    false
                }
            }
            else -> false
        }
    }

    override fun onBackPressed() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data){
            val uri = data.data.let { it } ?: return
            val filePathColumn = MediaStore.Images.Media.DATA
            val cursor = contentResolver.query(uri, arrayOf(filePathColumn),null,null,null)
            cursor?.moveToFirst()
            currentPhotoPath = cursor?.getString(cursor.getColumnIndex(filePathColumn))
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            graphicOverlay.visibility = View.GONE
            camera_preview.visibility = View.GONE
            control.visibility = View.INVISIBLE
            image_view.visibility = View.VISIBLE
            user.imagePath = currentPhotoPath
            image_view.setImageBitmap(bitmap)
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
            textRecognizer.processImage(image)
                    .addOnSuccessListener{
                        processTextRecognitionResult(it)
                    }
            cursor?.close()
        }
    }
}

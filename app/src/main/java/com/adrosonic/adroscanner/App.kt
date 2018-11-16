package com.adrosonic.adroscanner

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class App : Application() {

    lateinit var auth: FirebaseAuth

    companion object {

        @JvmStatic
        fun hasPermissions(context: Context?,permissions: Array<String>?): Boolean{
            if (context != null && permissions != null){
                permissions.forEach {permission ->
                    if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                        return false
                    }
                }
            }
            return true
        }

    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
    }

    //check if the device has camera
    fun checkCameraHardware(context: Context): Boolean{
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

}
package com.adrosonic.adroscanner

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.adrosonic.adroscanner.modules.login.MainActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.salesforce.androidsdk.analytics.security.Encryptor
import com.salesforce.androidsdk.app.SalesforceSDKManager

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
        SalesforceSDKManager.initNative(this,NativeKeyImpl(),MainActivity::class.java)
    }

    //check if the device has camera
    fun checkCameraHardware(context: Context): Boolean{
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

}

internal class NativeKeyImpl : SalesforceSDKManager.KeyInterface {

    override fun getKey(name: String): String {
        return Encryptor.hash(name + "12s9adpahk;n12-97sdainkasd=012", name + "12kl0dsakj4-cxh1qewkjasdol8")
    }
}
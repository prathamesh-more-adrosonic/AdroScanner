package com.adrosonic.adroscanner.modules.landing

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.databinding.DataBindingUtil.setContentView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.adrosonic.adroscanner.App
import com.adrosonic.adroscanner.modules.login.MainActivity
import com.adrosonic.adroscanner.R
import com.adrosonic.adroscanner.databinding.ActivityLandingBinding
import com.adrosonic.adroscanner.modules.camera.CameraActivity
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(this, R.layout.activity_landing) as ActivityLandingBinding
        if (checkCameraHardware(this)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),1)
        }
        else
            Toast.makeText(this,"Camera Not Present!!",Toast.LENGTH_SHORT).show()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this,CameraActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    val intent = Intent(this, CameraActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    //check if the device has camera
    private fun checkCameraHardware(context: Context): Boolean{
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
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
}

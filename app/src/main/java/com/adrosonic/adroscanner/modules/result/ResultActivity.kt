package com.adrosonic.adroscanner.modules.result

import android.content.ContentProviderOperation
import android.content.Intent
import androidx.databinding.DataBindingUtil.setContentView
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.adrosonic.adroscanner.App
import com.adrosonic.adroscanner.R
import com.adrosonic.adroscanner.databinding.ActivityResultBinding
import com.adrosonic.adroscanner.entity.UserEntity
import com.adrosonic.adroscanner.modules.camera.CameraActivity
import com.salesforce.androidsdk.app.SalesforceSDKManager
import com.salesforce.androidsdk.rest.ClientManager
import com.salesforce.androidsdk.rest.RestClient
import com.salesforce.androidsdk.rest.RestRequest
import com.salesforce.androidsdk.rest.RestResponse
import kotlinx.android.synthetic.main.activity_result.*
import okhttp3.MediaType
import okhttp3.RequestBody

class ResultActivity : AppCompatActivity() {

    private val ops = ArrayList<ContentProviderOperation>()
    private var user = UserEntity()
    var PERMISSION_ALL = 1
    val PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityResultBinding: ActivityResultBinding = setContentView(this,R.layout.activity_result)
        if ((applicationContext as App).checkCameraHardware(this)){
            if (!App.hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_ALL)
            }
        }
        else
            Toast.makeText(this,"Camera Not Present!!", Toast.LENGTH_SHORT).show()
        user = intent.getParcelableExtra("user")
        imageViewResult.maxHeight = windowManager.defaultDisplay.height/3
        imageViewResult.setImageBitmap(CameraActivity.image)
        Log.i("Result",user.toString())
        activityResultBinding.user = user
    }

    private fun saveContacts(){
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build())

        if (resultName.text != null){
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            resultName.text.toString()).build()
            )
        }
        if(resultPhone.text != null)
        {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, resultPhone.text.toString())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build()
            )
        }
        if(resultPhoneAlt.text != null)
        {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, resultPhoneAlt.text.toString())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE)
                    .build()
            )
        }
        if(resultEmail.text != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, resultEmail.text.toString())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build())
        }
        if(resultCompany.text != null && resultTitle.text != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, resultCompany.text.toString())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, resultTitle.text.toString())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build())
        }
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Contacts Saved")
            builder.setPositiveButton("OK"
            ) { dialog, _ ->
                startActivity(Intent(this,CameraActivity::class.java))
                dialog.dismiss()
            }
            builder.create()
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
            val builder = AlertDialog.Builder(this)
            builder.setMessage(e.message)
            builder.create()
            builder.show()
            //  Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

//    private fun createLeads(){
//        SalesforceSDKManager.getInstance().clientManager.getRestClient(this,object: ClientManager.RestClientCallback{
//            override fun authenticatedRestClient(client: RestClient?) {
//                val request = RestRequest(RestRequest.RestMethod.POST,
//                        "/services/apexrest/TataTrusts/PRM/newLeadService/",
//                        RequestBody.create(MediaType.parse("application/json"),
//                                "{\"data\":\"data\"}"))
//                client?.sendAsync(request,object: RestClient.AsyncRequestCallback{
//                    override fun onSuccess(request: RestRequest?, response: RestResponse?) {
//                        Log.i("Send Async",response.toString())
//                    }
//
//                    override fun onError(exception: java.lang.Exception?) {
//                        Log.i("Send Async",exception?.message)
//                    }
//
//                })
//            }
//
//        })
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_result,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.menuSave -> {
                saveContacts()
//                createLeads()
                return true
            }
            else -> false
        }
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    override fun onBackPressed() {
        startActivity(Intent(this,CameraActivity::class.java))
        finish()
    }
}

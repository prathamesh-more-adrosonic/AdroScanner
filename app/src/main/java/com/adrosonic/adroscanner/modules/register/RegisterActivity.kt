package com.adrosonic.adroscanner.modules.register

import android.content.Intent
import android.databinding.DataBindingUtil.setContentView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.adrosonic.adroscanner.App
import com.adrosonic.adroscanner.R
import com.adrosonic.adroscanner.databinding.ActivityRegisterBinding
import com.adrosonic.adroscanner.modules.landing.LandingActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

//    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val registerBinding = setContentView(this, R.layout.activity_register) as ActivityRegisterBinding
        val auth = (application.applicationContext as App).auth
        btn_register.setOnClickListener {
            val email = reg_email.text.toString()
            val pwd = reg_pwd.text.toString()
            try {
                if (email.isNotEmpty() && pwd.isNotEmpty()){
                    registerBinding.registerProgressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(this) {task ->
                        if (task.isSuccessful){
                            registerBinding.registerProgressBar.visibility = View.GONE
                            Toast.makeText(this,"User registration successful",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LandingActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            val errorCode = task.exception?.message.run { this } ?: "Unidentified Error"
                            registerBinding.registerProgressBar.visibility = View.GONE
                            Toast.makeText(this,errorCode,Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Entries are blank",Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                registerBinding.registerProgressBar.visibility = View.GONE
                Log.e("Registration Exception",e.message)
            }
        }
    }
}

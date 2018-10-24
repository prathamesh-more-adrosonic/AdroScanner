package com.adrosonic.adroscanner.modules.login

import android.content.Intent
import android.databinding.DataBindingUtil.setContentView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.adrosonic.adroscanner.App
import com.adrosonic.adroscanner.R
import com.adrosonic.adroscanner.modules.register.RegisterActivity
import com.adrosonic.adroscanner.databinding.ActivityMainBinding
import com.adrosonic.adroscanner.modules.landing.LandingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private lateinit var activityMainBinding: ActivityMainBinding
//    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = setContentView(this, R.layout.activity_main)
        var auth = (application.applicationContext as App).auth
        if (auth.currentUser != null){
            finish()
            startActivity(Intent(applicationContext, LandingActivity::class.java))
        }

        btn_login.setOnClickListener{
            val email: String = login_email.text.toString()
            val pwd: String = login_pwd.text.toString()
            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                activityMainBinding.loginProgressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        activityMainBinding.loginProgressBar.visibility = View.GONE
                        val intent = Intent(this, LandingActivity::class.java)
                        startActivity(intent)
                    }else{
                        activityMainBinding.loginProgressBar.visibility = View.GONE
                        val errorCode = task.exception?.message.run { this } ?: "Undefined Error"
                        Toast.makeText(this,errorCode,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        btn_signup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}

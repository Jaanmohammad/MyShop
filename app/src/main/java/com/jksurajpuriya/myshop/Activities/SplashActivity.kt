package com.jksurajpuriya.myshop.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.jksurajpuriya.myshop.MainActivity
import com.jksurajpuriya.myshop.R

class SplashActivity : AppCompatActivity() {
    var auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        auth= FirebaseAuth.getInstance()

        Handler().postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this@SplashActivity, AuthenticationActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }, 1500)
    }
}
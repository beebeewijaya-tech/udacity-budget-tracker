package com.beebee.mybudget.screens.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.beebee.mybudget.R
import com.beebee.mybudget.screens.auth.AuthActivity
import com.beebee.mybudget.screens.home.HomeActivity
import com.beebee.mybudget.utils.Token

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        navigateToOther()
    }

    private fun navigateToOther() {
        Handler().postDelayed({
            val token = Token.getToken(this)
            if (token.isEmpty()) {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1500)
    }
}
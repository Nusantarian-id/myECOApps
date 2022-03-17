package id.myeco.myeco.presentation.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import id.myeco.myeco.core.source.local.SessionManager
import id.myeco.myeco.databinding.ActivitySplashBinding
import id.myeco.myeco.utils.toastLong
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLoggedIn()
    }

    private fun isLoggedIn(){
        Handler(Looper.myLooper()!!).postDelayed({
            if (!sessionManager.isTokenExp())
                startActivity(Intent(this, MainActivity::class.java))
            else
                startActivity(Intent(this, LandingActivity::class.java))
        }, 2000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
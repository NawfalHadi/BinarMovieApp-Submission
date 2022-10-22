package com.thatnawfal.binarsibc6challange.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.presentation.logic.auth.AuthViewModel
import com.thatnawfal.binarsibc6challange.presentation.ui.auth.LoginBottomSheet
import com.thatnawfal.binarsibc6challange.presentation.ui.auth.OnLoginListener
import com.thatnawfal.binarsibc6challange.presentation.ui.auth.OnRegisterListener
import com.thatnawfal.binarsibc6challange.presentation.ui.auth.RegisterBottomSheet
import com.thatnawfal.binarsibc6challange.presentation.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            viewModel.getLoginStatus().observe(this){
                if (it) {
                    startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                    finish()
                } else {
                    showLoginBottomSheet()
                }
            }
        }, LOADING_TIMES)

    }

    private fun showLoginBottomSheet(){
        val currentDialog = supportFragmentManager.findFragmentByTag(LoginBottomSheet::class.java.simpleName)
        if (currentDialog == null) {
            LoginBottomSheet(object : OnLoginListener {
                override fun toRegister() {
                    showRegisterBottomSheet()
                }

                override fun loginSuccess() {
                    viewModel.setLoginStatus(true)
                    startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                    finish()
                }
            }).show(supportFragmentManager, LoginBottomSheet::class.java.simpleName)
        }
    }

    private fun showRegisterBottomSheet(){
        val currentDialog = supportFragmentManager.findFragmentByTag(RegisterBottomSheet::class.java.simpleName)
        if (currentDialog == null) {
            RegisterBottomSheet(object : OnRegisterListener {
                override fun toLogin() {
                    showLoginBottomSheet()
                }

            }).show(supportFragmentManager, RegisterBottomSheet::class.java.simpleName)
        }
    }

    companion object {
        private const val LOADING_TIMES = 3000L
    }
}
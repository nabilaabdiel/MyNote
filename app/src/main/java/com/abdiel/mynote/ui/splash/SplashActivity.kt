package com.abdiel.mynote.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.abdiel.mynote.R
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.databinding.ActivitySplashBinding
import com.abdiel.mynote.ui.home.HomeActivity
import com.abdiel.mynote.ui.login.LoginActivity
import com.crocodic.core.extension.openActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this,R.color.background)

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnLogin -> {
                viewModel.splash {
                    if (it) {
                        openActivity<HomeActivity>()
                    } else {
                        openActivity<LoginActivity>()
                    }
                    finish()
                }
            }
        }
        super.onClick(v)
    }
}
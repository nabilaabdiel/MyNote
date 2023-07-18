package com.abdiel.mynote.ui.profile

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdiel.mynote.R
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.databinding.ActivityProfileBinding
import com.abdiel.mynote.ui.login.LoginActivity
import com.abdiel.mynote.ui.update.password.UpdatePasswordActivity
import com.abdiel.mynote.ui.update.profile.UpdateProfileActivity
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.createIntent
import com.crocodic.core.extension.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>(R.layout.activity_profile) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.background)

        viewModel.getProfile() {
            getUser()
        }

        binding.btnEditProfile.setOnClickListener {
            activityLauncher.launch(createIntent<UpdateProfileActivity>()) {
                getUser()
            }
        }

        binding.btnEditPassword.setOnClickListener {
            openActivity<UpdatePasswordActivity>()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect { logout ->
                        if (logout.status == ApiStatus.SUCCESS) {
                            openActivity<LoginActivity>()
                            finishAffinity()
                        }
                    }
                }
            }
        }
    }

    private fun getUser() {
        val users = session.getUser()
        binding.user = users
    }
}
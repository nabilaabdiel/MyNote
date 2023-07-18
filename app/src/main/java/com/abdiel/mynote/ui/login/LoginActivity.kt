package com.abdiel.mynote.ui.login

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdiel.mynote.R
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.databinding.ActivityLoginBinding
import com.abdiel.mynote.ui.register.RegisterActivity
import com.abdiel.mynote.ui.forgot.send.SendEmailActivity
import com.abdiel.mynote.ui.home.HomeActivity
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnRegister.setOnClickListener {
            openActivity<RegisterActivity>()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etPhoneEmail.isEmptyRequired(R.string.label_must_fill) ||
                binding.etPassword.isEmptyRequired(R.string.label_must_fill)
            ) {
                return@setOnClickListener
            }

            val emailOrPhone = binding.etPhoneEmail.textOf()
            val password = binding.etPassword.textOf()

            viewModel.login(emailOrPhone, password)
        }

        binding.btnForgotPassword.setOnClickListener {
            openActivity<SendEmailActivity>()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("Logging in ..")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<HomeActivity>()
                                finishAffinity()
                            }
                            else -> loadingDialog.setResponse(it.message ?: return@collect)
                        }
                    }
                }
            }
        }
    }
}
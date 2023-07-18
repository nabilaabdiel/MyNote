package com.abdiel.mynote.ui.forgot.send

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdiel.mynote.R
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.databinding.ActivityLoginBinding
import com.abdiel.mynote.databinding.ActivitySendEmailBinding
import com.abdiel.mynote.ui.login.LoginViewModel
import com.crocodic.core.base.activity.NoViewModelActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendEmailActivity : NoViewModelActivity<ActivitySendEmailBinding>(R.layout.activity_send_email) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }
}
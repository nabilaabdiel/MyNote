package com.abdiel.mynote.base.activity

import androidx.databinding.ViewDataBinding
import com.abdiel.mynote.data.session.Session
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.viewmodel.CoreViewModel
import javax.inject.Inject

open class BaseActivity<VB: ViewDataBinding, VM: CoreViewModel>(layoutRes: Int): CoreActivity<VB, VM>(layoutRes) {
    @Inject
    lateinit var session: Session
}
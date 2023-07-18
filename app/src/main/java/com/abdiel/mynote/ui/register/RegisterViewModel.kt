package com.abdiel.mynote.ui.register

import androidx.lifecycle.viewModelScope
import com.abdiel.mynote.api.ApiService
import com.abdiel.mynote.base.viewModel.BaseViewModel
import com.abdiel.mynote.data.session.Session
import com.abdiel.mynote.data.user.User
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session

): BaseViewModel() {

    private val _users = MutableSharedFlow<User>()
    val users = _users.asSharedFlow()

    fun register( name: String, email: String, phone: String, password: String, passwordConfirmation: String )
    = viewModelScope.launch {
        _apiResponse.emit(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.register(name, email, phone, password, passwordConfirmation) },
            false, object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    _apiResponse.emit(ApiResponse().responseSuccess())
                }
            })
    }
}
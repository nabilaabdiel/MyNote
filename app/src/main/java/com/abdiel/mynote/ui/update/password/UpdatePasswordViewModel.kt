package com.abdiel.mynote.ui.update.password

import androidx.lifecycle.viewModelScope
import com.abdiel.mynote.api.ApiService
import com.abdiel.mynote.base.viewModel.BaseViewModel
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val apiService: ApiService
) : BaseViewModel() {

    fun changePassword(oldPassword: String, newPassword: String, passwordConfirmation: String)
            = viewModelScope.launch {
        _apiResponse.emit(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.changePassword(oldPassword, newPassword, passwordConfirmation) }, false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val message = response.getString(ApiCode.MESSAGE)
                    _apiResponse.emit(ApiResponse().responseSuccess(message))
                }
            })
    }
}
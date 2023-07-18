package com.abdiel.mynote.ui.note.delete

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abdiel.mynote.api.ApiService
import com.abdiel.mynote.base.viewModel.BaseViewModel
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DetailNotesViewModel @Inject constructor(
    private val apiService: ApiService
) : BaseViewModel() {

    protected val _deleteResponse = MutableSharedFlow<ApiResponse>() // private mutable shared flow
    val deleteResponse = _deleteResponse.asSharedFlow() // publicly exposed as read-only shared flow

    fun deleteNotes(id: String?) = viewModelScope.launch {
        _deleteResponse.emit(ApiResponse().responseLoading())
        ApiObserver({ apiService.delete(id) }, false, object : ApiObserver.ResponseListener {
            override suspend fun onSuccess(response: JSONObject) {
                _deleteResponse.emit(ApiResponse().responseSuccess())
            }
        })
    }

    fun favorite(id_note: String?, favorite: Boolean) = viewModelScope.launch {
        val paramData = JSONObject().apply {
            put("favorite", favorite)
        }

        val body = paramData.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        ApiObserver(
            { apiService.favNotes(id_note, body) }, false, object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    _apiResponse.emit(ApiResponse().responseSuccess("success", data = favorite))
                }

                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                }
            })
    }
}
package com.abdiel.mynote.ui.note.edit

import androidx.lifecycle.viewModelScope
import com.abdiel.mynote.api.ApiService
import com.abdiel.mynote.base.viewModel.BaseViewModel
import com.abdiel.mynote.data.session.Session
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson
): BaseViewModel() {

    fun editNotes(id: String?, title: String, body: String)
            = viewModelScope.launch {
        ApiObserver(
            { apiService.editNotes(id, "PATCH", title, body) }, false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val message = response.getString(ApiCode.MESSAGE)
                    _apiResponse.emit(ApiResponse().responseSuccess(message))
                }
            })
    }
}
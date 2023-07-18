package com.abdiel.mynote.ui.note

import androidx.lifecycle.viewModelScope
import com.abdiel.mynote.api.ApiService
import com.abdiel.mynote.base.viewModel.BaseViewModel
import com.abdiel.mynote.data.category.Category
import com.abdiel.mynote.data.note.Note
import com.abdiel.mynote.data.session.Session
import com.abdiel.mynote.data.user.User
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toList
import com.crocodic.core.extension.toObject
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson
) : BaseViewModel() {

    private val _categories = MutableSharedFlow<List<Category>>()
    val categories = _categories.asSharedFlow()

    fun createNote(title: String, body: String, categoryId: String) = viewModelScope.launch {
        _apiResponse.emit(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.createNotes(title, body) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
//                    _apiResponse.emit(ApiResponse().responseSuccess())
                    val data = response.getJSONObject("data").toObject<Note>(gson)
                    addCategoryToNote(data.id, categoryId)
                }

                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                }
            })
    }

    fun getCategories() = viewModelScope.launch {
        ApiObserver(
            {apiService.getCategories()},
            false, object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val data = response.getJSONArray(ApiCode.DATA).toList<Category>(gson)
                    _categories.emit(data)
                }
            })
    }

    fun addCategoryToNote(idNote: String?, categoryId: String) = viewModelScope.launch {
        ApiObserver(
            {apiService.addCategoryToNote(idNote, categoryId)},
            false, object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    _apiResponse.emit(ApiResponse().responseSuccess())
                }
            })
    }
}
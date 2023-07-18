package com.abdiel.mynote.ui.home

import androidx.lifecycle.viewModelScope
import com.abdiel.mynote.api.ApiService
import com.abdiel.mynote.base.viewModel.BaseViewModel
import com.abdiel.mynote.data.category.Category
import com.abdiel.mynote.data.session.Session
import com.abdiel.mynote.data.note.Note
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toList
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson
) : BaseViewModel() {

    private val _listNotes = MutableSharedFlow<List<Note>>()
    val listNotes = _listNotes.asSharedFlow()

    private val _categories = MutableSharedFlow<List<Category>>()
    val categories = _categories.asSharedFlow()

    fun getNotes(filter: String? = null) = viewModelScope.launch {
        ApiObserver({ apiService.getNotes() }, false, object : ApiObserver.ResponseListener {
            override suspend fun onSuccess(response: JSONObject) {
                val data = response.getJSONArray(ApiCode.DATA).toList<Note>(gson)
                if (filter.isNullOrEmpty()) {
                    _listNotes.emit(data)
                } else {
                    _listNotes.emit(data.filter { it.categories.contains(Category(categoryName = filter)) })
                }
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
}
package com.abdiel.mynote

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.data.constant.Const
import com.abdiel.mynote.data.note.Note
import com.abdiel.mynote.databinding.ActivityEditNoteBinding
import com.abdiel.mynote.ui.note.edit.EditNoteViewModel
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.textOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditNoteActivity :
    BaseActivity<ActivityEditNoteBinding, EditNoteViewModel>(R.layout.activity_edit_note) {

    private var note: Note? = null
//    private var title: String? = null
//    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.background)

//        title = intent.getStringExtra("title")
//        content = intent.getStringExtra("content")

        note = intent.getParcelableExtra(Const.NOTES.NOTES)
        binding.data = note
        Log.d("check", "note: $note")

        binding.btnSave.setOnClickListener {

            val title = binding.etTitle.textOf()
            val content = binding.etContent.textOf()

            viewModel.editNotes(note?.id, title, content)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("saved")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                setResult(Const.NOTES.NOTES_EDITED)
                                finish()
                            }

                            else -> loadingDialog.setResponse(it.message ?: return@collect)
                        }
                    }
                }
            }
        }
    }
}
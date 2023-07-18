package com.abdiel.mynote.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdiel.mynote.R
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.data.category.Category
import com.abdiel.mynote.data.constant.Const
import com.abdiel.mynote.databinding.ActivityAddNoteBinding
import com.abdiel.mynote.ui.note.AddNoteViewModel
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.textOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteActivity : BaseActivity<ActivityAddNoteBinding, AddNoteViewModel>(R.layout.activity_add_note) {

    private var categoryId = ""
    private val categories = ArrayList<Category?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.background)

        binding.spinnerCategory.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = categories[p2]
                categoryId = item?.id ?: ""
            }
        }

        viewModel.getCategories()

        binding.btnSave.setOnClickListener {

            if (binding.etTitle.isEmptyRequired(R.string.label_must_fill) ||
                binding.etContent.isEmptyRequired(R.string.label_must_fill)
            ) {
                return@setOnClickListener
            }

            val title = binding.etTitle.textOf()
            val content = binding.etContent.textOf()

            viewModel.createNote(title, content, categoryId)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("saved")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                setResult(Const.NOTES.NOTES_ADD)
                                finish()
                            }
                            else -> loadingDialog.setResponse(it.message ?: return@collect)
                        }
                    }
                }

                launch {
                    viewModel.categories.collect {
                        val arrayAdapter = ArrayAdapter(this@AddNoteActivity, R.layout.dropdown_item, it)
                        binding.spinnerCategory.setAdapter(arrayAdapter)
                        categories.clear()
                        categories.addAll(it)
                    }
                }
            }
        }
    }
}
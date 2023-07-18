package com.abdiel.mynote.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdiel.mynote.DetailNotesActivity
import com.abdiel.mynote.R
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.data.category.Category
import com.abdiel.mynote.data.constant.Const
import com.abdiel.mynote.data.note.Note
import com.abdiel.mynote.databinding.ActivityHomeBinding
import com.abdiel.mynote.databinding.ListItemBinding
import com.abdiel.mynote.ui.AddNoteActivity
import com.abdiel.mynote.ui.profile.ProfileActivity
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.createIntent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    private var listNoteHome = ArrayList<Note?>()
    private var listAllNoteHome = ArrayList<Note?>()
    private var categoryId = ""
    private val categories = ArrayList<Category?>()

    private val adapter by lazy {
        //Adapter
        CoreListAdapter<ListItemBinding, Note>(R.layout.list_item)
            .initItem(listNoteHome) { position, data ->
                activityLauncher.launch(createIntent<DetailNotesActivity> {
                    putExtra(Const.NOTES.NOTES, data)
                }) {
                    if (it.resultCode == Const.NOTES.NOTES_EDITED ||
                        it.resultCode == Const.NOTES.NOTES_DELETE ||
                        it.resultCode == Const.NOTES.NOTES_FAV) {
                        viewModel.getNotes()
                    }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.background)

        binding.spinnerCategory.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = categories[p2]
                categoryId = item?.id ?: ""
                viewModel.getNotes(item?.categoryName)
            }
        }

        viewModel.getCategories()

        getUser()
        observe()
        iniSwipe()

        binding.svHome.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                listNoteHome.clear()
                adapter.notifyDataSetChanged()
                listNoteHome.addAll(listAllNoteHome)
                adapter.notifyItemInserted(0)
            } else {
                val filter = listAllNoteHome.filter { it?.title?.contains("$text", true) == true }
                listNoteHome.clear()
                adapter.notifyDataSetChanged()
                listNoteHome.addAll(filter)
                adapter.notifyItemInserted(0)
            }
            binding.tvBlankData.isVisible = listNoteHome.isEmpty()
        }

        binding.btnProfile.setOnClickListener {
            activityLauncher.launch(createIntent<ProfileActivity>()) {
                getUser()
            }
        }

        binding.rvHome.adapter = adapter

        viewModel.getNotes()

        binding.btnCreate.setOnClickListener {
            activityLauncher.launch(createIntent<AddNoteActivity>()) {
                if (it.resultCode == Const.NOTES.NOTES_ADD) {
                    viewModel.getNotes()
                }
            }
        }
    }

    //data user
    private fun getUser() {
        val users = session.getUser()
        binding.data = users
    }

//    private fun load(data: Note) {
//        activityLauncher.launch(createIntent<DetailActivity> {

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.listNotes.collect { notes ->
                        listNoteHome.clear()
                        listAllNoteHome.clear()
                        adapter.notifyDataSetChanged()
                        listNoteHome.addAll(notes)
                        listAllNoteHome.addAll(notes)
                        adapter.notifyItemInserted(0)
                        binding.tvBlankData.isVisible = notes.isEmpty()
                        binding.swipeRefresh.isRefreshing = false
                    }
                }

                launch {
                    viewModel.categories.collect {
                        val arrayAdapter = ArrayAdapter(this@HomeActivity, R.layout.dropdown_item, it)
                        binding.spinnerCategory.setAdapter(arrayAdapter)
                        categories.clear()
                        categories.addAll(it)
                    }
                }
            }
        }
    }

    private fun iniSwipe() {
        binding.swipeRefresh.setProgressViewOffset(false, 0, 280)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getNotes()
        }
    }
}


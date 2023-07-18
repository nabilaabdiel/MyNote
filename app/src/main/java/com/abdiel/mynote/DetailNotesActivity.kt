package com.abdiel.mynote

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.abdiel.mynote.base.activity.BaseActivity
import com.abdiel.mynote.data.constant.Const
import com.abdiel.mynote.data.note.Note
import com.abdiel.mynote.databinding.ActivityDetailNotesBinding
import com.abdiel.mynote.ui.note.delete.DetailNotesViewModel
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.createIntent
import com.crocodic.core.extension.snacked
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailNotesActivity : BaseActivity<ActivityDetailNotesBinding, DetailNotesViewModel>(R.layout.activity_detail_notes) {

    private var note : Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.background)

        binding.btnBack.setOnClickListener {
            finish()
        }

        note = intent.getParcelableExtra(Const.NOTES.NOTES)
        binding.data = note

        initBtnSave()

        binding.borderFavorite.setOnClickListener {
            viewModel.favorite(note?.id, true)
            binding.root.snacked("Add to favorite")

        }

        binding.fillFavorite.setOnClickListener {
            viewModel.favorite(note?.id, false)
            binding.root.snacked("Remove from favorite")
        }

        binding.share.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, note?.body)
            startActivity(Intent.createChooser(shareIntent,getString(R.string.send_to)))
        }

        binding.btnEdit.setOnClickListener {
            activityLauncher.launch(createIntent<EditNoteActivity>{
                putExtra(Const.NOTES.NOTES, note)
            }) {
                if (it.resultCode == Const.NOTES.NOTES_EDITED) {
                    setResult(Const.NOTES.NOTES_EDITED)
                    finish()
                }
            }
        }

        binding.delete.setOnClickListener {
            unsavedAlert()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.deleteResponse.collect {
                        if (it.status == ApiStatus.SUCCESS) {
                            setResult(Const.NOTES.NOTES_DELETE)
                            finish()
                        }
                    }
                }

                launch {
                    viewModel.apiResponse.collect {
                        if (it.status == ApiStatus.SUCCESS) {
                            note?.favorite = it.dataAs()
                            initBtnSave()
                            setResult(Const.NOTES.NOTES_FAV)
                        }
                    }
                }
            }
        }
    }
    private fun unsavedAlert() {
        val alertDialog = LayoutInflater.from(this)
            .inflate(R.layout.alert_dialog, findViewById(R.id.alert_dialog))

        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(alertDialog)

        val dialog = alertDialogBuilder.show()
        dialog.window?.setBackgroundDrawableResource(R.color.white)

        val btnCancel = alertDialog.findViewById<AppCompatButton>(R.id.btn_cancel)
        val btnConfirm = alertDialog.findViewById<AppCompatButton>(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            viewModel.deleteNotes(note?.id)
            dialog.dismiss()
        }
    }

    private fun initBtnSave() {
        if (note?.favorite == true) {
            binding.borderFavorite.visibility = View.GONE
            binding.fillFavorite.visibility = View.VISIBLE
        } else {
            binding.borderFavorite.visibility = View.VISIBLE
            binding.fillFavorite.visibility = View.GONE
        }
    }
}
package com.abdiel.mynote.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.abdiel.mynote.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.helper.StringHelper


class ViewBindingHelper {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imageUrl"], requireAll = true)
        fun loadImageRecipe(view: ImageView, imageUrl: String?) {

            view.post {
                view.setImageDrawable(null)

                imageUrl?.let {
                    Glide
                        .with(view.context)
                        .load(StringHelper.validateEmpty(imageUrl))
                        .placeholder(R.drawable.ic_person)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.ic_round_error_outline_24)
                        .into(view)
                }
            }
        }
    }
}
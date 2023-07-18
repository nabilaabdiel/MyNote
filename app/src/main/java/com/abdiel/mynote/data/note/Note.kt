package com.abdiel.mynote.data.note


import android.os.Parcelable
import com.abdiel.mynote.data.category.Category
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    @SerializedName("body")
    val body: String?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("category_id")
    val categoryId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("favorite")
    var favorite: Boolean?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("updated_at_format")
    val updatedAtFormat: String?,
    @SerializedName("categories")
    val categories: List<Category>

) : Parcelable
package com.abdiel.mynote.data.category


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("note_category_id")
    val noteCategoryId: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("total_notes")
    val totalNotes: Int? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
) : Parcelable {
    override fun toString(): String {
        return categoryName ?: ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false
        if (this.categoryName == other.categoryName) return true
        return false
    }

    override fun hashCode(): Int {
        return categoryName.hashCode()
    }
}
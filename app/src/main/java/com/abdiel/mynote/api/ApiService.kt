package com.abdiel.mynote.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    //Login
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email_or_phone") phone: String?,
        @Field("password") password: String?
    ): String

    //Register
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") password_confirmation: String?
    ): String

    //Logout
    @DELETE("auth/logout")
    suspend fun logout(): String

    //Get Profile
    @GET("auth/me")
    suspend fun getProfile(): String

    //Update Profile
    @FormUrlEncoded
    @POST("user/profile")
    suspend fun editProfile(
        @Field("name") name: String?
    ): String

    //Update Profile
    @Multipart
    @POST("user/profile")
    suspend fun editProfileImg(
        @Part("name") name: String?,
        @Part photo: MultipartBody.Part?
    ): String

    //Change Password
    @FormUrlEncoded
    @POST("auth/change-password")
    suspend fun changePassword(
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String,
        @Field("password_confirmation") password_confirmation: String
    ): String

    //Get Notes
    @GET("notes")
    suspend fun getNotes(): String

    //Get Favorite
    @GET("notes/favorite")
    suspend fun getFav(): String

    //Get Notes By Categories
    @GET("categories/:id/notes")
    suspend fun getNoteByCategories(): String

    //Get Notes By Id
    @GET("notes/{id}")
    suspend fun getNoteById(): String

    //Favorite Notes
    @POST("notes/{id_note}/favorite")
    suspend fun favNotes(
        @Path("id_note") id_note: String?,
        @Body favorite: RequestBody
    ): String

    //Create Notes
    @FormUrlEncoded
    @POST("notes")
    suspend fun createNotes(
        @Field("title") title: String?,
        @Field("body") body: String?
//        @Field("category_id") category_id: String?
    ): String

    //Edit Notes
    @FormUrlEncoded
    @POST("notes/{id}")
    suspend fun editNotes(
        @Path("id") id: String?,
        @Field("_method") method: String?,
        @Field("title") title: String?,
        @Field("body") body: String?
//        @Field("category_id") category_id: String?
    ): String

    //Delete Notes
    @DELETE("notes/{id}")
    suspend fun delete(
        @Path("id") id: String?
    ): String

    //Get Category
    @GET("categories")
    suspend fun getCategories(
    ): String

    //Create Categories
    @FormUrlEncoded
    @POST("categories")
    suspend fun createCategories(
        @Field("category_name") category_name: String?
    ): String

    //addCategoryToNote
    @FormUrlEncoded
    @POST("notes/{idNote}/category")
    suspend fun addCategoryToNote(
        @Path("idNote") idNote: String?,
        @Field("category_id") category_id: String?
    ): String
}
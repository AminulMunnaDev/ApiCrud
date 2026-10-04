package com.example.apihttpmethods

import com.google.gson.annotations.SerializedName

// Generic State Wrapper for UI & Repo Layer
sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String) : Resource<Nothing>
}

// Data Model matching ReqRes user item
data class User(
    val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
    val avatar: String? = null,
    val job: String? = null
)

// Wrapper for ReqRes GET /users list response
data class UserListResponse(
    val page: Int,
    val data: List<User>
)

// Request Body for POST and PUT
data class UserRequest(
    val name: String,
    val job: String
)

// Response Body for POST / PUT / PATCH
data class UserMutationResponse(
    val id: String?,
    val name: String?,
    val job: String?,
    val createdAt: String?,
    val updatedAt: String?
)
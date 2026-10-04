package com.example.apihttpmethods

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(private val apiService: ApiService) {

    fun getUsers(page: Int = 1): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getUsers(page)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.data))
            } else {
                emit(Resource.Error("GET error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to reach server"))
        }
    }

    fun createUser(name: String, job: String): Flow<Resource<UserMutationResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.createUser(UserRequest(name, job))
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("POST failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Network error"))
        }
    }

    fun updateUser(id: Int, name: String, job: String): Flow<Resource<UserMutationResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.updateUser(id, UserRequest(name, job))
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("PUT failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Network error"))
        }
    }

    fun patchUser(id: Int, updates: Map<String, String>): Flow<Resource<UserMutationResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.patchUser(id, updates)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("PATCH failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Network error"))
        }
    }

    fun deleteUser(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.deleteUser(id)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("DELETE failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Network error"))
        }
    }
}
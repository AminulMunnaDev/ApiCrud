package com.example.apihttpmethods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository = UserRepository(NetworkClient.apiService)
) : ViewModel() {

    private val _usersState = MutableStateFlow<Resource<List<User>>>(Resource.Loading)
    val usersState: StateFlow<Resource<List<User>>> = _usersState.asStateFlow()

    private val _actionState = MutableStateFlow<Resource<String>?>(null)
    val actionState: StateFlow<Resource<String>?> = _actionState.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            repository.getUsers().collect { result ->
                _usersState.value = result
            }
        }
    }

    fun createUser(name: String, job: String) {
        viewModelScope.launch {
            repository.createUser(name, job).collect { result ->
                _actionState.value = when (result) {
                    is Resource.Loading -> Resource.Loading
                    is Resource.Success -> Resource.Success(
                        "POST Success [201 Created]\nID: ${result.data.id}\nName: ${result.data.name}\nJob: ${result.data.job}\nAt: ${result.data.createdAt}"
                    )
                    is Resource.Error -> Resource.Error(result.message)
                }
            }
        }
    }

    fun updateUser(id: Int, name: String, job: String) {
        viewModelScope.launch {
            repository.updateUser(id, name, job).collect { result ->
                _actionState.value = when (result) {
                    is Resource.Loading -> Resource.Loading
                    is Resource.Success -> Resource.Success(
                        "PUT Success [200 OK]\nReplaced User #$id\nName: ${result.data.name}\nJob: ${result.data.job}\nUpdated At: ${result.data.updatedAt}"
                    )
                    is Resource.Error -> Resource.Error(result.message)
                }
            }
        }
    }

    fun patchUser(id: Int, job: String) {
        viewModelScope.launch {
            repository.patchUser(id, mapOf("job" to job)).collect { result ->
                _actionState.value = when (result) {
                    is Resource.Loading -> Resource.Loading
                    is Resource.Success -> Resource.Success(
                        "PATCH Success [200 OK]\nPatched Job for User #$id\nNew Job: ${result.data.job}\nUpdated At: ${result.data.updatedAt}"
                    )
                    is Resource.Error -> Resource.Error(result.message)
                }
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            repository.deleteUser(id).collect { result ->
                _actionState.value = when (result) {
                    is Resource.Loading -> Resource.Loading
                    is Resource.Success -> Resource.Success("DELETE Success [204 No Content]\nUser #$id removed successfully.")
                    is Resource.Error -> Resource.Error(result.message)
                }
            }
        }
    }
}
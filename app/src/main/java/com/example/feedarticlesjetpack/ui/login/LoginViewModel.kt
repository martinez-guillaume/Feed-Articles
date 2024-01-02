package com.example.feedarticlesjetpack.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import com.example.feedarticlesjetpack.network.ApiResponse
import com.example.feedarticlesjetpack.network.LoginDto
import com.example.feedarticlesjetpack.network.ResourceProvider
import com.example.feedarticlesjetpack.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

    private val apiArticleInterface: ApiArticleInterface,
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider

) : ViewModel() {
    private val _loginResult = MutableLiveData<ApiResponse>()
    val loginResult: LiveData<ApiResponse> = _loginResult

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = apiArticleInterface.loginUser(username, password)

            if (response?.isSuccessful == true) {
                response.body()?.let { apiResponse ->
                    _loginResult.value = apiResponse
                    when (apiResponse.status) {
                        1 -> {
                            onLoginSuccess(apiResponse)
                            _message.value = resourceProvider.getString(R.string.login_success)
                        }
                        5 -> _message.value = resourceProvider.getString(R.string.authenticated_token_unchanged)
                        0 -> _message.value = resourceProvider.getString(R.string.user_unknown)
                        -1 -> _message.value = resourceProvider.getString(R.string.parameter_error_login)
                        else -> _message.value = resourceProvider.getString(R.string.error_unknown)
                    }
                } ?: run {
                    _loginResult.value = ApiResponse(status = -1)
                    _message.value = resourceProvider.getString(R.string.login_failed)
                }
            } else {
                _loginResult.value = ApiResponse(status = -1)
                _message.value = resourceProvider.getString(R.string.login_failed)
            }
        }
    }



    private fun onLoginSuccess(apiResponse: ApiResponse) {
        val userId = apiResponse.id ?: 0
        userRepository.saveUserId(userId)
        sharedPreferences.edit().putString("token", apiResponse.token).apply()
    }

}


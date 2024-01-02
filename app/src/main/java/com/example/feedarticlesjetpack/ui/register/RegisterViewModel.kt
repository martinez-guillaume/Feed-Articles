package com.example.feedarticlesjetpack.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import com.example.feedarticlesjetpack.network.ApiResponse
import com.example.feedarticlesjetpack.network.RegisterDto
import com.example.feedarticlesjetpack.network.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

    private val apiArticleInterface: ApiArticleInterface,
    private val resourceProvider: ResourceProvider

) : ViewModel() {

    private val _registerResult = MutableLiveData<ApiResponse>()
    val registerResult: LiveData<ApiResponse> = _registerResult

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val response = apiArticleInterface.registerUser(RegisterDto(username, password))

            if (response?.isSuccessful == true) {
                response.body()?.let { apiResponse ->
                    _registerResult.value = apiResponse
                    when (apiResponse.status) {
                        1 -> _toastMessage.value =
                            resourceProvider.getString(R.string.toast_success_register)

                        5 -> _toastMessage.value =
                            resourceProvider.getString(R.string.toast_login_already_used)

                        0 -> _toastMessage.value =
                            resourceProvider.getString(R.string.toast_register_failure)

                        -1 -> _toastMessage.value =
                            resourceProvider.getString(R.string.toast_parameter_error)

                        else -> _toastMessage.value =
                            resourceProvider.getString(R.string.error_try_again_register_fragment)
                    }
                } ?: run {
                    _registerResult.value = ApiResponse(status = -1)
                    _toastMessage.value =
                        resourceProvider.getString(R.string.error_try_again_register_fragment)
                }
            } else {
                _registerResult.value = ApiResponse(status = -1)
                _toastMessage.value =
                    resourceProvider.getString(R.string.error_try_again_register_fragment)
            }
        }
    }
}

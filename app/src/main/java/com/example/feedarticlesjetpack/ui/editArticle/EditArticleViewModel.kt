package com.example.feedarticlesjetpack.ui.editArticle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import com.example.feedarticlesjetpack.network.ApiResponse
import com.example.feedarticlesjetpack.network.ResourceProvider
import com.example.feedarticlesjetpack.network.UpdateArticleDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditArticleViewModel @Inject constructor (

    private val apiArticleInterface: ApiArticleInterface,
    private val resourceProvider: ResourceProvider

) : ViewModel() {

    private val _deleteStatus = MutableLiveData<ApiResponse>()
    val deleteStatus: LiveData<ApiResponse> = _deleteStatus

    private val _updateStatus = MutableLiveData<ApiResponse>()
    val updateStatus: LiveData<ApiResponse> = _updateStatus

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun deleteArticle(articleId: Long, token: String) {
        viewModelScope.launch {
            val response = apiArticleInterface.deleteArticle(articleId, token)
            response?.body()?.let {
                _deleteStatus.value = it
                _message.value = when (it.status) {
                    1 -> resourceProvider.getString(R.string.article_deleted_success)
                    0 -> resourceProvider.getString(R.string.no_deletion)
                    -1 -> resourceProvider.getString(R.string.parameter_problem)
                    -5 -> resourceProvider.getString(R.string.unauthorized_deletion)
                    else -> resourceProvider.getString(R.string.error_try_again)
                }
            }
        }
    }
    fun updateArticle(articleId: Long, title: String, content: String, imageUrl: String, category: Int, token: String) {
        viewModelScope.launch {
            val updateArticleDto = UpdateArticleDto(id = articleId, title = title, desc = content, image = imageUrl, cat = category)
            val response = apiArticleInterface.updateArticle(articleId, token, updateArticleDto)
            response?.body()?.let {
                _updateStatus.value = it
                _message.value = when (it.status) {
                    1 -> resourceProvider.getString(R.string.article_updated_success)
                    0 -> resourceProvider.getString(R.string.no_modification)
                    -1 -> resourceProvider.getString(R.string.parameter_problem)
                    -2 -> resourceProvider.getString(R.string.ids_different)
                    -5 -> resourceProvider.getString(R.string.unauthorized_modification)
                    else -> resourceProvider.getString(R.string.error_try_again)
                }
            }
        }
    }
}
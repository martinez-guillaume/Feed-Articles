package com.example.feedarticlesjetpack.ui.detailsArticle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import com.example.feedarticlesjetpack.network.ArticleDto
import com.example.feedarticlesjetpack.network.ResourceProvider
import com.example.feedarticlesjetpack.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsArticleViewModel @Inject constructor(

    private val apiArticleInterface: ApiArticleInterface,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider

) : ViewModel() {

    private val _articleDetails = MutableLiveData<ArticleDto?>()
    val articleDetails: LiveData<ArticleDto?> = _articleDetails

    private val _favorisMessage = MutableLiveData<String>()
    val favorisMessage: LiveData<String> = _favorisMessage

    fun fetchArticleDetails(articleId: Long) {
        viewModelScope.launch {
            try {
                val token = userRepository.getTokenFromPreferences() ?: ""
                val response = apiArticleInterface.getArticle(articleId, withFav = 1, token)

                if (response?.isSuccessful == true) {
                    _articleDetails.value = response.body()?.article
                } else {
                    _favorisMessage.value = resourceProvider.getString(R.string.error_retrieving_details)
                }
            } catch (e: Exception) {
                _favorisMessage.value = resourceProvider.getString(R.string.error_retrieving_details)
            }
        }
    }

    fun toggleFavoriteStatus(articleId: Long) {
        viewModelScope.launch {

            val token = userRepository.getTokenFromPreferences() ?: ""
            val response = apiArticleInterface.updateFavoriteStatus(articleId, token)

            when {
                response?.isSuccessful == true && response.body()?.status == 1 -> {
                    _articleDetails.value = _articleDetails.value?.let { currentArticle ->
                        val isCurrentlyFavorited = currentArticle.is_fav == 1
                        currentArticle.copy(is_fav = if (isCurrentlyFavorited) 0 else 1).also {
                            _favorisMessage.value = if (isCurrentlyFavorited) {
                                resourceProvider.getString(R.string.favorite_removed)
                            } else {
                                resourceProvider.getString(R.string.favorite_added)
                            }
                        }
                    }
                }
                response?.body()?.status == 0 -> {
                    _favorisMessage.value = resourceProvider.getString(R.string.favorite_status_unchanged)
                }
                response?.body()?.status == -1 -> {
                    _favorisMessage.value = resourceProvider.getString(R.string.parameter_error)
                }
                response?.body()?.status == -5 -> {
                    _favorisMessage.value = resourceProvider.getString(R.string.unauthorized_operation)
                }
                else -> {
                    _favorisMessage.value = resourceProvider.getString(R.string.error_unexpected)
                }
            }
        }
    }
}

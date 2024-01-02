package com.example.feedarticlesjetpack.ui.creaArticle


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import com.example.feedarticlesjetpack.network.NewArticleDto
import com.example.feedarticlesjetpack.network.ResourceProvider
import com.example.feedarticlesjetpack.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreaArticleViewModel  @Inject constructor (

    private val apiArticleInterface: ApiArticleInterface,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider

) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _navigateToMainFragment = MutableLiveData(false)
    val navigateToMainFragment: LiveData<Boolean>
        get() = _navigateToMainFragment

    fun createArticle(title: String, desc: String, image: String, cat: Int) {
        // checker formulaire
        val userId = userRepository.getUserIdFromPreferences()
        val token = userRepository.getTokenFromPreferences() ?: return

        if (title.length > 80) {
            _message.postValue(resourceProvider.getString(R.string.title_length_error))
            return
        }
        viewModelScope.launch {

            val newArticle = NewArticleDto(userId, title, desc, image, cat)


            // dispatcher , attention au thread (with context)


            val response = apiArticleInterface.createArticle(token, newArticle)

            when (response?.isSuccessful) {
                true -> {
                    val apiResponse = response.body()
                    when (apiResponse?.status) {
                        1 -> {
                            _message.postValue(resourceProvider.getString(R.string.article_creation_success))
                            _navigateToMainFragment.value = true
                        }
                        0, -1 -> _message.postValue(resourceProvider.getString(R.string.article_creation_failure))
                        -5 -> _message.postValue(resourceProvider.getString(R.string.creation_unauthorized))
                        else -> _message.postValue(resourceProvider.getString(R.string.unknown_error))
                    }
                }
                else -> _message.postValue(resourceProvider.getString(R.string.unknown_error))
            }
        }
    }
}
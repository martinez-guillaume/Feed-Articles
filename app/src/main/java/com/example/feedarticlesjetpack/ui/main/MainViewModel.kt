package com.example.feedarticlesjetpack.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import com.example.feedarticlesjetpack.network.ArticleDto
import com.example.feedarticlesjetpack.network.ResourceProvider
import com.example.feedarticlesjetpack.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (

    private val apiArticleInterface: ApiArticleInterface,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider

) : ViewModel() {

    // mauvaise idée , une seule liste et non 4

    private val _allArticles = MutableLiveData<List<ArticleDto>>()

    private val _sportArticles = MutableLiveData<List<ArticleDto>>()

    private val _mangaArticles = MutableLiveData<List<ArticleDto>>()

    private val _diversArticles = MutableLiveData<List<ArticleDto>>()

    private val _showFavorites = MutableLiveData<Boolean>(false)
    val showFavorites: LiveData<Boolean> = _showFavorites

    private val _filteredArticles = MutableLiveData<List<ArticleDto>>()
    val filteredArticles: LiveData<List<ArticleDto>> = _filteredArticles

    private val _currentCategory = MutableLiveData<Category>(Category.ALL)

    private val _navigateToLogin = MutableLiveData<Boolean>(false)
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    enum class Category {
        ALL, SPORT, MANGA, DIVERSE
    }

    init {
        loadArticles()
    }

    fun toggleFavoriteFilter() {
        _showFavorites.value = !showFavorites.value!!
        filterArticles()
    }
    private fun filterArticles() {
        val currentCategoryArticles = when (_currentCategory.value) {
            Category.SPORT -> _sportArticles.value
            Category.MANGA -> _mangaArticles.value
            Category.DIVERSE -> _diversArticles.value
            else -> _allArticles.value
        }

        val filtered = if (showFavorites.value!!) {
            currentCategoryArticles?.filter { it.is_fav == 1 } ?: emptyList()
        } else {
            currentCategoryArticles ?: emptyList()
        }

        _filteredArticles.postValue(filtered)
    }
    fun setCurrentCategory(category: Category) {
        _currentCategory.value = category
        filterArticles()
    }

    fun getUserId(): Long {
        return userRepository.getUserIdFromPreferences()
    }


    fun refreshArticles() {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            val token = userRepository.getTokenFromPreferences() ?: ""
            val response = apiArticleInterface.getArticles(1, token)

            response?.let {
                if (it.isSuccessful) {
                    val articles = it.body()?.articles ?: emptyList()
                    _allArticles.postValue(articles)
                    _sportArticles.postValue(articles.filter { article -> article.categorie == 1 })
                    _mangaArticles.postValue(articles.filter { article -> article.categorie == 2 })
                    _diversArticles.postValue(articles.filter { article -> article.categorie == 3 })
                    _filteredArticles.postValue(articles)
                } else {
                    resourceProvider.getString(R.string.error_try_again_main_fragment)
                }
            } ?: run {
                resourceProvider.getString(R.string.error_try_again_main_fragment)
            }
            _isLoading.value = false
        }
    }

    fun performLogout() {
        _navigateToLogin.value = true
    }
}
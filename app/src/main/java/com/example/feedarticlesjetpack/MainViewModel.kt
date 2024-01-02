package com.example.feedarticlesjetpack

import androidx.lifecycle.ViewModel
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val apiCountryInterface: ApiArticleInterface
) : ViewModel() {
}
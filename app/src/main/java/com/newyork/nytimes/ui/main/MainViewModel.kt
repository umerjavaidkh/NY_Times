package com.newyork.nytimes.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newyork.nytimes.data.repository.NewsRepository
import com.newyork.nytimes.model.Article
import com.newyork.nytimes.model.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [MainActivity]
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(private val articleRepository: NewsRepository) :
    ViewModel() {

    private val _articles: MutableStateFlow<State<List<Article>>> = MutableStateFlow(State.loading())

    val articles: StateFlow<State<List<Article>>> = _articles

    fun getArticles() {
        viewModelScope.launch {
            articleRepository.getAllArticles()
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _articles.value = state }
        }
    }
}

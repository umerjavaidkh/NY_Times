package com.newyork.nytimes.ui.details


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.newyork.nytimes.data.repository.NewsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * ViewModel for [ArticleDetailsActivity]
 */
@ExperimentalCoroutinesApi
class ArticleDetailsViewModel @AssistedInject constructor(
    articleRepository: NewsRepository,
    @Assisted articleId: Long
) : ViewModel() {

    val article = articleRepository.getArticleById(articleId).asLiveData()

    @AssistedFactory
    interface ArticleDetailsViewModelFactory {
        fun create(articleId: Long): ArticleDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: ArticleDetailsViewModelFactory,
            articleId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(articleId) as T
            }
        }
    }
}

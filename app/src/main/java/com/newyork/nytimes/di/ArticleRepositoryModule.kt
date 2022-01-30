package  com.newyork.nytimes.di

import com.newyork.nytimes.data.repository.DefaultArticleRepository
import com.newyork.nytimes.data.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Currently PostRepository is only used in ViewModels.
 * PostDetailsViewModel is not injected using @HiltViewModel so can't install in ViewModelComponent.
 */
@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ArticleRepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindPostRepository(repository: DefaultArticleRepository): NewsRepository
}

package  com.newyork.nytimes.di
import android.app.Application
import com.newyork.nytimes.data.local.NYArticlesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NYTimesDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = NYArticlesDatabase.getInstance(application)

    @Singleton
    @Provides
    fun providePostsDao(database: NYArticlesDatabase) = database.getArticlesDao()
}

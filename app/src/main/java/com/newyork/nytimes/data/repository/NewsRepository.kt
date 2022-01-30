package com.newyork.nytimes.data.repository

import androidx.annotation.MainThread
import com.newyork.nytimes.data.local.dao.ArticlesDao
import com.newyork.nytimes.data.remote.api.NYNewsService
import com.newyork.nytimes.model.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import retrofit2.Response
import javax.inject.Inject

interface NewsRepository {
    fun getAllArticles(): Flow<Resource<List<Article>>>
    fun getArticleById(id: Long): Flow<Article>
}

/**
 * Singleton repository for fetching data from remote and storing it in database
 * for offline capability. This is Single source of data.
 */
@ExperimentalCoroutinesApi
class DefaultArticleRepository @Inject constructor(
    private val articlesDao: ArticlesDao,
    private val nyNewsService: NYNewsService
) : NewsRepository {

    /**
     * Fetched the posts from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    override fun getAllArticles(): Flow<Resource<List<Article>>> {
        return object : NetworkBoundRepository<List<Article>, List<Article>>() {

            override suspend fun saveRemoteData(response: List<Article>) = articlesDao.addArticles(response)

            override fun fetchFromLocal(): Flow<List<Article>> = articlesDao.getAllArticles()

            override suspend fun fetchFromRemote(): Response<List<Article>> {
                var  result =  nyNewsService.getArticles()
                try {
                    return result?.let {
                        var data = mapArticlesDataItem(it)
                        return@let Response.success(data!!)
                    } as Response<List<Article>>
                } catch (e: Exception) {
                    return  Response.error(result.code(),result.errorBody())
                }
            }
        }.asFlow()
    }

     fun mapArticlesDataItem(response: Response<ArticlesResponse>):List<Article>? {
       var inwrap = response.body()
        var articles = inwrap?.articles?.map {
            Article(
                it.id,
                if (!it.media.isNullOrEmpty()) it.media?.get(0)?.mediaMetaData?.get(2)?.url else "",
                it.title,
                it.byline,
                it.abstractX,
                it.publishedDate,
                it.url,
                if (!it.media.isNullOrEmpty()) it.media?.get(0)?.mediaMetaData?.get(1)?.url else ""
            )
        }

        return articles
    }

    /**
     * Retrieves a post with specified [Id].
     * @param Id Unique id of a [Article].
     * @return [Article] data fetched from the database.
     */
    @MainThread
    override fun getArticleById(id: Long): Flow<Article> {

        return articlesDao.getArticleById(id).distinctUntilChanged()
    }
}

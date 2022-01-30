

package com.newyork.nytimes.data.remote.api

import com.newyork.nytimes.BuildConfig
import com.newyork.nytimes.data.repository.ArticlesResponse
import com.newyork.nytimes.model.Article
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * Service to fetch NYNews articles using  end point [NY_TIMES_API_URL].
 */
interface NYNewsService {

    @GET("/svc/mostpopular/v2/viewed/{period}.json")
    suspend fun getArticles(@Path("period") period: Int = 7,
                            @Query("api-key") apiKey: String = BuildConfig.API_KEY): Response<ArticlesResponse>


    //https://api.nytimes.com/svc/mostpopular/v2/emailed/7.json?api-key=NklDh6oq4hHAAK1v8nH8j3Ggc1PagBZG
}

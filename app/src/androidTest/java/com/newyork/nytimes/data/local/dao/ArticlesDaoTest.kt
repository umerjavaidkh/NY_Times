package com.newyork.nytimes.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.newyork.nytimes.data.local.NYArticlesDatabase
import com.newyork.nytimes.model.Article
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesDaoTest {

    private lateinit var mDatabase: NYArticlesDatabase

    @Before
    fun init() {
        mDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NYArticlesDatabase::class.java
        ).build()
    }

    @Test
    @Throws(InterruptedException::class)
    fun insert_and_select_articles() = runBlocking {
        val articles = listOf(
            Article(1, "Test 1", "Test 1", "Test 1",abstractX = "abc",coverImageUrl = "url:123",publishedDate = "2022-06-01",url = "https//image1"),
            Article(2, "Test 2", "Test 2", "Test 3",abstractX = "bcd",coverImageUrl = "url:132",publishedDate = "2022-08-01",url = "https//image2")
        )

        mDatabase.getArticlesDao().addArticles(articles)

        val dbArticles = mDatabase.getArticlesDao().getAllArticles().first()

        assertThat(dbArticles, equalTo(articles))
    }

    @Test
    @Throws(InterruptedException::class)
    fun select_post_by_id() = runBlocking {
        val articles = listOf(
            Article(1, "Test 1", "Test 1", "Test 1",abstractX = "abc",coverImageUrl = "url:123",publishedDate = "2022-06-01",url = "https//image1"),
            Article(2, "Test 2", "Test 2", "Test 3",abstractX = "bcd",coverImageUrl = "url:132",publishedDate = "2022-08-01",url = "https//image2")
        )

        mDatabase.getArticlesDao().addArticles(articles)

        var dbPdbArticle = mDatabase.getArticlesDao().getArticleById(1).first()
        assertThat(dbPdbArticle, equalTo(articles[0]))

        dbPdbArticle = mDatabase.getArticlesDao().getArticleById(2).first()
        assertThat(dbPdbArticle, equalTo(articles[1]))
    }

    @After
    fun cleanup() {
        mDatabase.close()
    }
}

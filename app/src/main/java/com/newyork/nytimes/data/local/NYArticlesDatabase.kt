package com.newyork.nytimes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.newyork.nytimes.data.local.dao.ArticlesDao
import com.newyork.nytimes.model.Article



/**
 * Abstract NY  Article database.
 * It provides DAO [ArticlesDao] by using method [getArticlesDao]
 */
@Database(
    entities = [Article::class],
    version = DatabaseMigrations.DB_VERSION
)
abstract class NYArticlesDatabase : RoomDatabase() {

    /**
     * @return [ArticleDao] NY Articles Data Access Object.
     */
    abstract fun getArticlesDao(): ArticlesDao

    companion object {
        const val DB_NAME = "ny_articles_database"

        @Volatile
        private var INSTANCE: NYArticlesDatabase? = null

        fun getInstance(context: Context): NYArticlesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NYArticlesDatabase::class.java,
                    DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}

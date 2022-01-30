package com.newyork.nytimes.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ShareCompat
import coil.load
import com.newyork.nytimes.R
import com.newyork.nytimes.databinding.ActivityArticleDetailsBinding
import com.newyork.nytimes.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ArticleDetailsActivity : BaseActivity<ArticleDetailsViewModel, ActivityArticleDetailsBinding>() {

    @Inject
    lateinit var viewModelFactory: ArticleDetailsViewModel.ArticleDetailsViewModelFactory

    override val mViewModel: ArticleDetailsViewModel by viewModels {
        val articleId = intent.extras?.getLong(KEY_ARTICLE_ID)
            ?: throw IllegalArgumentException("`Article` must be non-null")

        ArticleDetailsViewModel.provideFactory(viewModelFactory,  articleId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        initArticle()
    }

    private fun initArticle() {
        mViewModel.article.observe(this) { article ->
            mViewBinding.articleContent.apply {
                articleTitle.text = article.title
                articleAuthor.text = article.byline
                articleBody.text = article.abstractX
            }
            mViewBinding.imageView.load(article.imageUrl)
        }
    }

    private fun share() {
        val article = mViewModel.article.value ?: return
        val shareMsg = getString(R.string.share_message, article.title, article.abstractX)

        val intent = ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setText(shareMsg)
            .intent

        startActivity(Intent.createChooser(intent, null))
    }

    override fun getViewBinding(): ActivityArticleDetailsBinding =
        ActivityArticleDetailsBinding.inflate(layoutInflater)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }

            R.id.action_share -> {
                share()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val KEY_ARTICLE_ID = "article_id"

        fun getStartIntent(
            context: Context,
            articleId: Long
        ) = Intent(context, ArticleDetailsActivity::class.java).apply { putExtra(KEY_ARTICLE_ID, articleId) }
    }
}

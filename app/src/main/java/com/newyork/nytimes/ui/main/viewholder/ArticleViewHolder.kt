package com.newyork.nytimes.ui.main.viewholder

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.newyork.nytimes.R
import coil.load
import com.newyork.nytimes.databinding.ItemArticleBinding
import com.newyork.nytimes.model.Article


/**
 * [RecyclerView.ViewHolder] implementation to inflate View for RecyclerView.
 * See [ArticleListAdapter]]
 */
class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article, onItemClicked: (Article, ImageView) -> Unit) {
        binding.articleTitle.text = article.title
        binding.articleAuthor.text = article.byline
        binding.imageView.load(article.imageUrl) {
            placeholder(R.drawable.ic_photo)
            error(R.drawable.ic_broken_image)
        }

        binding.root.setOnClickListener {
            onItemClicked(article, binding.imageView)
        }
    }
}

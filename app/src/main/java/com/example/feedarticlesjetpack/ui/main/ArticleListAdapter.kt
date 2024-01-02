package com.example.feedarticlesjetpack.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.ItemRvArticleBinding
import com.example.feedarticlesjetpack.network.ArticleDto
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class ArticleListAdapter(private val onArticleClicked: (ArticleDto) -> Unit) : ListAdapter<ArticleDto, ArticleListAdapter.ArticleViewHolder>(DiffCallback()) {

    class ArticleViewHolder(val binding: ItemRvArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleDto, onArticleClick: (ArticleDto) -> Unit) {


            // affichage du titre
            val shortTitle = if (article.titre.length > 18) {
                article.titre.substring(0, 18) + "..."
            } else {
                article.titre
            }
            binding.tvTitleItemRv.text = shortTitle

            // affichage du contenu texte , xml text view ... dans xml
            val shortContent = if (article.descriptif.length > 35) {
                article.descriptif.substring(0, 35) + "..."
            } else {
                article.descriptif
            }
            binding.textArticleItemRv.text = shortContent

            // Date
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).parse(article.created_at)?.let { outputFormat.format(it) }
            binding.dateItemRv.text = formattedDate

            // change background color
         when (article.categorie) {
                1 -> R.color.colorSportBackground
                2 -> R.color.colorMangaBackground
                else -> R.color.white
            }.let{
             binding.clItemArticle.setBackgroundResource(it)
         }


            // Favoris
            if (article.is_fav == 1) {
                binding.ivStarOnItemRv.visibility = View.VISIBLE
            } else {
                binding.ivStarOnItemRv.visibility = View.GONE
            }

            //picture :
            if (!article.url_image.isNullOrEmpty()) {
                Picasso.get()
                    .load(article.url_image)
                    .placeholder(R.drawable.feedarticles_logo)
                    .error(R.drawable.feedarticles_logo)
                    .into(binding.pictureItemRv)
            } else {
                // si l'URL est vide ou nulle
                binding.pictureItemRv.setImageResource(R.drawable.feedarticles_logo)
            }
            binding.root.setOnClickListener {
                onArticleClick(article)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleDto>() {
        override fun areItemsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleDto, newItem: ArticleDto): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRvArticleBinding.inflate(layoutInflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, onArticleClicked)
    }
}

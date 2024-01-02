package com.example.feedarticlesjetpack.ui.detailsArticle

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentDetailsArticleBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class DetailsArticleFragment : Fragment() {

    private lateinit var binding: FragmentDetailsArticleBinding
    private val viewModel: DetailsArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_article, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this

        val articleId = DetailsArticleFragmentArgs.fromBundle(requireArguments()).articleId

        viewModel.articleDetails.observe(viewLifecycleOwner) { article ->
            binding.article = article
        }
        viewModel.favorisMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchArticleDetails(articleId)

        return binding.root
    }

    fun onBackClicked() {
        findNavController().navigateUp()
    }
    fun onStarClicked() {
        binding.article?.id?.let { articleId ->
            viewModel.toggleFavoriteStatus(articleId)
        }
    }
    fun formatDate(dateString: String?): String {
        return dateString?.let {
                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formatter = SimpleDateFormat(" dd/MM/yyyy", Locale.getDefault())
                formatter.format(parser.parse(it) ?: return "")} ?: ""
    }
    fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            1 -> "Sport"
            2 -> "Manga"
            3 -> "Divers"
            else -> "Inconnu"
        }
    }
}
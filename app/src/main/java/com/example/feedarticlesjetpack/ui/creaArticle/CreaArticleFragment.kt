package com.example.feedarticlesjetpack.ui.creaArticle

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentCreaArticleBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreaArticleFragment : Fragment() {

    private var _binding: FragmentCreaArticleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreaArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreaArticleBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCreaArticleBinding.bind(view)

        binding.btnSubmitCreaArticleActivity.setOnClickListener {
            val title = binding.etTitleCreaArticleFragment.text.toString()
            val content = binding.etContentCreaFragment.text.toString()
            val imageUrl = binding.etPictureCreaArticleFragment.text.toString()
            val category = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rb_sport_crea_article_fragment -> 1
                R.id.rb_manga_crea_article_fragment -> 2
                else -> 3
            }
            viewModel.createArticle(title, content, imageUrl, category)

            viewModel.navigateToMainFragment.observe(viewLifecycleOwner) { shouldNavigate ->
                if (shouldNavigate) {
                    findNavController().popBackStack()
                }
            }
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }


        binding.etPictureCreaArticleFragment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                val imageUrl = s?.toString()
                if (!imageUrl.isNullOrEmpty()) {
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.feedarticles_logo)
                        .error(R.drawable.feedarticles_logo)
                        .into(binding.ivCreaArticleFragment)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
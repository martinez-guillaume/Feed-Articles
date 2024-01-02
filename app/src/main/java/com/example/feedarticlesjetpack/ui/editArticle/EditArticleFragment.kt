package com.example.feedarticlesjetpack.ui.editArticle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentEditArticleBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditArticleFragment : Fragment() {

    private var _binding: FragmentEditArticleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditArticleViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleId = arguments?.getLong("articleId")
        val title = arguments?.getString("title")
        val content = arguments?.getString("content")
        val imageUrl = arguments?.getString("imageUrl")

        binding.etTitleEditFragment.setText(title)
        binding.etContentEditFragment.setText(content)
        binding.etUrlEditFragment.setText(imageUrl)

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.feedarticles_logo)
                .error(R.drawable.feedarticles_logo)
                .into(binding.ivEditFragment)
        }

        binding.btnDeleteEditFragment.setOnClickListener {
            articleId?.let { id ->
                val token = sharedPreferences.getString("token", "") ?: ""
                viewModel.deleteArticle(id, token)
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.deleteStatus.observe(viewLifecycleOwner) { apiResponse ->
            if (apiResponse.status == 1) {
                findNavController().navigate(R.id.action_editArticleFragment_to_mainFragment)
            }
        }

        binding.btnSubmitEditFragment.setOnClickListener {
            val newTitle = binding.etTitleEditFragment.text.toString()
            val newContent = binding.etContentEditFragment.text.toString()
            val newImageUrl = binding.etUrlEditFragment.text.toString()
            val newCategory = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rb_sport_edit_fragment -> 1
                R.id.rb_manga_edit_fragment -> 2
                R.id.rb_diverse_edit_fragment -> 3
                else -> 0
            }
            val token = sharedPreferences.getString("token", "") ?: ""

            articleId?.let { id ->
                viewModel.updateArticle(id, newTitle, newContent, newImageUrl, newCategory, token)
            }
        }

        viewModel.updateStatus.observe(viewLifecycleOwner){ apiResponse ->
            if (apiResponse.status == 1) {
                findNavController().navigate(R.id.action_editArticleFragment_to_mainFragment)
            }
        }


        binding.etUrlEditFragment.addTextChangedListener(object : TextWatcher {
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
                        .into(binding.ivEditFragment)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
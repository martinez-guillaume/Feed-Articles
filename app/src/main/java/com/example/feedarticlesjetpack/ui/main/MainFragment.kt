package com.example.feedarticlesjetpack.ui.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = viewModel.getUserId()

        val adapter = ArticleListAdapter { article ->
            if (article.id_u == userId) {
                val bundle = Bundle()
                bundle.putLong("articleId", article.id)
                bundle.putString("title", article.titre)
                bundle.putString("content", article.descriptif)
                bundle.putString("imageUrl", article.url_image)
                findNavController().navigate(R.id.action_mainFragment_to_editArticleFragment, bundle)
            } else {
                val action = MainFragmentDirections.actionMainFragmentToDetailsArticleFragment(article.id)
                findNavController().navigate(action)
            }
        }

        binding.rvMainFragment.adapter = adapter
        binding.rvMainFragment.layoutManager = LinearLayoutManager(requireContext())

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val category = when (checkedId) {
                R.id.rb_sport_main_fragment -> MainViewModel.Category.SPORT
                R.id.rb_manga_main_fragment -> MainViewModel.Category.MANGA
                R.id.rb_diverse_main_fragment -> MainViewModel.Category.DIVERSE
                else -> MainViewModel.Category.ALL
            }
            viewModel.setCurrentCategory(category)
        }

        viewModel.filteredArticles.observe(viewLifecycleOwner, Observer { articles ->
            adapter.submitList(articles)
        })

        binding.ivStarMainFragment.setOnClickListener {
            viewModel.toggleFavoriteFilter()
        }

        viewModel.showFavorites.observe(viewLifecycleOwner, Observer { showFavorites ->
            if (showFavorites) {
                binding.ivStarMainFragment.setImageResource(android.R.drawable.btn_star_big_on)
            } else {
                binding.ivStarMainFragment.setImageResource(android.R.drawable.btn_star_big_off)
            }
        })

        binding.ivAddMainFragment.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_creaArticleFragment)
        }

        binding.ivLogoutMainFragment.setOnClickListener {
            viewModel.performLogout()
        }

        viewModel.navigateToLogin.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshArticles()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBarMainFragment.visibility = View.GONE
            } else {
                binding.progressBarMainFragment.visibility = View.VISIBLE
            }
        }
        viewModel.refreshArticles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

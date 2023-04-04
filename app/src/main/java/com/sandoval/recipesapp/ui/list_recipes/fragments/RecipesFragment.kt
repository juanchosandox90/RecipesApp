package com.sandoval.recipesapp.ui.list_recipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandoval.recipesapp.R
import com.sandoval.recipesapp.databinding.FragmentRecipesBinding
import com.sandoval.recipesapp.ui.list_recipes.adapter.RecipesAdapter
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.MainViewModel
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.RecipesViewModel
import com.sandoval.recipesapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private var _recipesFragmentBinding: FragmentRecipesBinding? = null
    private val recipesFragmentBinding get() = _recipesFragmentBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel =
            ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel =
            ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipesFragmentBinding = FragmentRecipesBinding.inflate(layoutInflater)

        setupRecyclerView()
        requestApiData()

        return recipesFragmentBinding.root
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recipesFragmentBinding.recyclerview.adapter = mAdapter
        recipesFragmentBinding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        recipesFragmentBinding.shimmerFrameLayout.startShimmer()
        recipesFragmentBinding.shimmerFrameLayout.visibility = View.VISIBLE
        recipesFragmentBinding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        recipesFragmentBinding.shimmerFrameLayout.stopShimmer()
        recipesFragmentBinding.shimmerFrameLayout.visibility = View.GONE
        recipesFragmentBinding.recyclerview.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesFragmentBinding = null
    }
}
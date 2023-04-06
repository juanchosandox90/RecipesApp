package com.sandoval.recipesapp.ui.list_recipes.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandoval.recipesapp.R
import com.sandoval.recipesapp.databinding.FragmentRecipesBinding
import com.sandoval.recipesapp.ui.list_recipes.adapter.RecipesAdapter
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.MainViewModel
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.RecipesViewModel
import com.sandoval.recipesapp.utils.NetworkListener
import com.sandoval.recipesapp.utils.NetworkResult
import com.sandoval.recipesapp.utils.hideKeyboard
import com.sandoval.recipesapp.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(),SearchView.OnQueryTextListener {

    private val args: RecipesFragmentArgs by navArgs()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var networkListener: NetworkListener

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
        recipesFragmentBinding.lifecycleOwner = this
        recipesFragmentBinding.mainViewModel = mainViewModel

        setupRecyclerView()

        recipesViewModel.readBackOnline?.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect() { status ->
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        recipesFragmentBinding.floatingActionButton.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }

        }


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu)

                val search = menu.findItem(R.id.menuSearch)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@RecipesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return recipesFragmentBinding.root
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            hideKeyboard()
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    /** At first implementation the observer was being called twice, first if DB was empty
     * requestApiData and after that as the observer keep observing, and the DB was not empty
     * anymore, called mAdapter.setData(database[0].foodRecipe), means called the data from the DB
     * and DB entity. So this is solved using a property from observers called ObserveOnce from my
     * Extension Functions. **/
    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
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

    private fun searchApiData(searchQuery: String) {
        Log.d("RecipesFragment", "searchApiData called!")
        showShimmerEffect()
        mainViewModel.searchRecipes(
            recipesViewModel.applySearchQuery(
                searchQuery
            )
        )
        mainViewModel.searchRecipesResponse.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    hideShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recipesFragmentBinding.recyclerview.adapter = mAdapter
        recipesFragmentBinding.recyclerview.layoutManager =
            LinearLayoutManager(requireContext())
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
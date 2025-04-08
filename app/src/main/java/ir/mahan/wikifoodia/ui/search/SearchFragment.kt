package ir.mahan.wikifoodia.ui.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.adapter.RecipeItemsAdapter
import ir.mahan.wikifoodia.databinding.FragmentSearchBinding
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.ui.recipe.RecipeFragmentDirections
import ir.mahan.wikifoodia.utils.NetworkChecker
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.setupRecyclerview
import ir.mahan.wikifoodia.utils.showSnackBar
import ir.mahan.wikifoodia.viewmodels.SearchViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates


@AndroidEntryPoint
class SearchFragment : Fragment() {

    // binding object
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var recipeItemsAdapter: RecipeItemsAdapter
    @Inject
    lateinit var netWorkChecker: NetworkChecker

    private val viewModel: SearchViewModel by viewModels()
    private var isNetworkAvailable by Delegates.notNull<Boolean>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecentAdapter()
        binding.apply {
            //Keyboard listener
            requireActivity().window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                requireActivity().window.decorView.getWindowVisibleDisplayFrame(rect)
                val height = requireActivity().window.decorView.height
                if (height - rect.bottom <= height * 0.1399)
                    rootMotion.transitionToStart()
                else
                    rootMotion.transitionToEnd()
            }
            //Check internet
            lifecycleScope.launch {
                netWorkChecker.observeNetworkState().collect { isAvailable ->
                    initInternetLayout(isAvailable)
                    isNetworkAvailable = isAvailable
                }
            }
            // Search
            searchEdt.addTextChangedListener {
                if (it.toString().length>2 && isNetworkAvailable)
                    viewModel.searchRecipes(viewModel.searchQueries(it.toString()))
            }
        }
        loadSearchResult()
    }

    private fun loadSearchResult() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseWrapper.Loading -> {
                    binding.searchList.showShimmer()
                }

                is ResponseWrapper.Success -> {
                    it.data?.let { responseRecipes ->
                        if (!responseRecipes.results.isNullOrEmpty()) {
                            binding.searchList.hideShimmer()
                            fillRecipeAdapter(responseRecipes.results)
                        }
                    }
                }

                is ResponseWrapper.Error -> {
                    binding.searchList.hideShimmer()
                    binding.root.showSnackBar(it.message.toString())
                }
            }
        }
    }

    private fun fillRecipeAdapter(result: List<ResponseRecipes.Result>) {
        recipeItemsAdapter.setData(result)
    }

    private fun initRecentAdapter() {
        binding.searchList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            recipeItemsAdapter
        )
        recipeItemsAdapter.setOnItemClickListener {
            val action = RecipeFragmentDirections.actionToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetLay.isVisible = isConnected.not()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package ir.mahan.wikifoodia.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.adapter.PopularItemsAdapter
import ir.mahan.wikifoodia.adapter.RecipeItemsAdapter
import ir.mahan.wikifoodia.databinding.FragmentRecipeBinding
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.utils.NetworkChecker
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.observeOnce
import ir.mahan.wikifoodia.utils.setupRecyclerview
import ir.mahan.wikifoodia.viewmodels.RecipesViewmodel
import ir.mahan.wikifoodia.viewmodels.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    val registerViewModel: RegisterViewModel by viewModels()
    val recipeViewModel: RecipesViewmodel by viewModels()
    @Inject
    lateinit var popularItemsAdapter: PopularItemsAdapter
    @Inject
    lateinit var recipeItemsAdapter: RecipeItemsAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Show username
        lifecycleScope.launchWhenCreated { showUsername() }
        // Fetch Data
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                initPopularAdapter()
                initRecentAdapter()
                networkChecker.observeNetworkState().collect { isNetworkAvailable ->
                    if (isNetworkAvailable) {
                        // Call Apis
                        recipeViewModel.callPopularApi(recipeViewModel.popularQueries())
                        recipeViewModel.callRecentApi(recipeViewModel.recentQueries())
                    } else {
                        loadCachedPopularFoods()
                        loadCachedRecentFoods()
                    }
                }
            }
        }
        loadPopularFoods()
        loadRecentFoods()
    }


    //---Popular---//
    private fun loadPopularFoods() {
        recipeViewModel.popularData.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseWrapper.Loading -> {
                    // Show Loading
                    setupLoading(true, binding.popularList)
                }
                is ResponseWrapper.Success -> {
                    it.data?.let { responseRecipes ->
                        if (!responseRecipes.results.isNullOrEmpty()) {
                            // Hide Loading
                            setupLoading(false, binding.popularList)
                            fillPopularAdapter(it.data.results!!.toMutableList())
                        }
                    }
                }
                is ResponseWrapper.Error -> {
                    // Hide Loading
                    setupLoading(false, binding.popularList)
                }
            }
        }
    }

    private fun loadCachedPopularFoods() {
        recipeViewModel.popularFoodsFromDB.observeOnce(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                it[0].responseRecipes.results?.let {
                    setupLoading(false, binding.popularList)
                    fillPopularAdapter(it)
                }
            }
        }
    }

    private fun fillPopularAdapter(result: List<ResponseRecipes.Result>) {
        popularItemsAdapter.setData(result)
        setAutoScroll(result)
    }

    private fun initPopularAdapter() {
        val snapHelper = LinearSnapHelper()
        binding.popularList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            popularItemsAdapter
        )
        //Snap
        binding.popularList.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.popularList)
        popularItemsAdapter.setOnItemClickListener {
            // Todo: Go to Detail Screen
            val action = RecipeFragmentDirections.actionToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setAutoScroll(results: List<ResponseRecipes.Result>) {
        lifecycleScope.launch {
            repeat(Constants.REPEAT_TIME) {
                results.indices.forEach { index ->
                    delay(Constants.DELAY_TIME)
                    binding.popularList.smoothScrollToPosition(index)
                }
            }
        }
    }

    //---Recent---//
    private fun loadRecentFoods() {
        recipeViewModel.recentData.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseWrapper.Loading -> {
                    // Show Loading
                    setupLoading(true, binding.recipesList)
                }
                is ResponseWrapper.Success -> {
                    it.data?.let { responseRecipes ->
                        if (!responseRecipes.results.isNullOrEmpty()) {
                            // Hide Loading
                            setupLoading(false, binding.recipesList)
                            fillRecipeAdapter(responseRecipes.results)
                        }
                    }
                }
                is ResponseWrapper.Error -> {
                    // Hide Loading
                    setupLoading(false, binding.recipesList)
                }
            }
        }
    }

    private fun loadCachedRecentFoods() {
        recipeViewModel.recentFoodsFromDB.observeOnce(viewLifecycleOwner) {
            Timber.d("Database Item 1 Size: ${it[1].responseRecipes.results?.size}")
            if (it.isNotEmpty()) {
                it[1].responseRecipes.results?.let { recentFoods ->
                    setupLoading(false, binding.recipesList)
                    fillRecipeAdapter(recentFoods)
                }
            }
        }
    }

    private fun fillRecipeAdapter(result: List<ResponseRecipes.Result>) {
        recipeItemsAdapter.setData(result)
        setAutoScroll(result)
    }

    private fun initRecentAdapter() {
        binding.recipesList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            recipeItemsAdapter
        )
        recipeItemsAdapter.setOnItemClickListener {
            // Todo: Go to Detail Screen
            val action = RecipeFragmentDirections.actionToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }


    //---General Functions---///
    private fun setupLoading(isShownLoading: Boolean, shimmer: ShimmerRecyclerView) {
        shimmer.apply {
            if (isShownLoading) showShimmer() else hideShimmer()
        }
    }

    @SuppressLint("SetTextI18n")
    suspend fun showUsername() {
        registerViewModel.datastoreRegisterData.collect {
            binding.usernameTxt.text = "${getString(R.string.hello)}, ${it.username} ${getEmojiByUnicode()}"
        }
    }

    private fun getEmojiByUnicode(): String {
        return String(Character.toChars(0x1f44b))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
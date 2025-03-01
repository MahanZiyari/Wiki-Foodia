package ir.mahan.wikifoodia.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.adapter.PopularItemsAdapter
import ir.mahan.wikifoodia.adapter.RecipeItemsAdapter
import ir.mahan.wikifoodia.databinding.FragmentRecipeBinding
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.ResponseWrapper
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Show username
        lifecycleScope.launchWhenCreated { showUsername() }
        // Call Api
        recipeViewModel.callPopularApi(recipeViewModel.popularQueries())
        recipeViewModel.callRecentApi(recipeViewModel.recentQueries())
        // Load Data
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
                            initPopularAdapter(responseRecipes.results)
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
    private fun initPopularAdapter(results: List<ResponseRecipes.Result>) {
        val snapHelper = LinearSnapHelper()
        popularItemsAdapter.setData(results)
        binding.popularList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            popularItemsAdapter
        )
        //Snap
        binding.popularList.onFlingListener = null
        snapHelper.attachToRecyclerView(binding.popularList)
        popularItemsAdapter.setOnItemClickListener {
            // Todo: Go to Detail Screen
        }
        setAutoScroll(results)
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
        Timber.d("\nInit Recents Food")
        recipeViewModel.recentData.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseWrapper.Loading -> {
                    // Show Loading
                    setupLoading(true, binding.recipesList)
                }
                is ResponseWrapper.Success -> {
                    it.data?.let { responseRecipes ->
                        Timber.d("total Results: ${responseRecipes.totalResults}")
                        if (!responseRecipes.results.isNullOrEmpty()) {
                            // Hide Loading
                            Timber.d("${responseRecipes.results.size}")
                            setupLoading(false, binding.recipesList)
                            initRecentAdapter(responseRecipes.results)
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
    private fun initRecentAdapter(results: List<ResponseRecipes.Result>) {
        Timber.d("\nInit Recents Adapter with: $results")
        recipeItemsAdapter.setData(results)
        binding.recipesList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            recipeItemsAdapter
        )
        recipeItemsAdapter.setOnItemClickListener {
            // Todo: Go to Detail Screen
        }
    }


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
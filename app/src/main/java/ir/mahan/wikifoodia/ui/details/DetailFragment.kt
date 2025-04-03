package ir.mahan.wikifoodia.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat.setTint
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.adapter.InstructionsAdapter
import ir.mahan.wikifoodia.adapter.SimilarAdapter
import ir.mahan.wikifoodia.adapter.StepsAdapter
import ir.mahan.wikifoodia.data.database.entity.FavoriteEntity
import ir.mahan.wikifoodia.databinding.FragmentDetailBinding
import ir.mahan.wikifoodia.models.detail.ResponseDetail
import ir.mahan.wikifoodia.models.detail.ResponseSimilar
import ir.mahan.wikifoodia.utils.NetworkChecker
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.convertMinToHour
import ir.mahan.wikifoodia.utils.setDynamicallyColor
import ir.mahan.wikifoodia.utils.setTint
import ir.mahan.wikifoodia.utils.setupRecyclerview
import ir.mahan.wikifoodia.utils.showSnackBar
import ir.mahan.wikifoodia.utils.switchVisibilityBy
import ir.mahan.wikifoodia.viewmodels.DetailViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : Fragment() {

    // binding object
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var instructionsAdapter: InstructionsAdapter
    @Inject lateinit var stepsAdapter: StepsAdapter
    @Inject lateinit var similarAdapter: SimilarAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //ViewModel
    private val viewModel: DetailViewmodel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var recipeId = 0
    private var isCached = false
    private var isFromFavorite = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.let {
            recipeId = args.foodID
            if (recipeId > 0) {
                Timber.d("Recipe ID: $recipeId")
                checkForCachedDetail()
                viewModel.checkForFavoriteExistence(recipeId)
            }
        }// end of args
        // Handle UI
        binding.apply {
            //Back
            backImg.setOnClickListener { findNavController().popBackStack() }
            // Data Management
            loadDetailsByAppropriateSource()

        }// end of binding
    }// end of onViewCreated

    private fun FragmentDetailBinding.loadDetailsByAppropriateSource() {
        lifecycleScope.launch {
            networkChecker.observeNetworkState().collect { isAvailable ->
                delay(200)
                when (isAvailable) {
                    true -> {
                        Timber.d("Cache: $isCached  Network: $isAvailable")
                        if (isCached) {
                            loadCachedDetail()
                        } else loadDetailFromApi()
                        loadSimilarItemsByApi()
                    }

                    false -> {
                        Timber.d("Cache: $isCached  Network: $isAvailable")
                        if (isCached)
                            loadCachedDetail()
                        else
                            internetLay.isVisible = true
                    }
                }
            }
        }
    }
    // Api Calls
    private fun FragmentDetailBinding.loadDetailFromApi() {
        viewModel.getFoodDetailsByApi(recipeId)
        viewModel.latestDetailData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseWrapper.Loading -> {
                    binding.loading.switchVisibilityBy(contentLay)
                }

                is ResponseWrapper.Success -> {
                    it.data?.let { responseDetail ->
                        fillViews(responseDetail)
                        binding.contentLay.switchVisibilityBy(loading)
                    }
                }

                is ResponseWrapper.Error -> {
                    binding.contentLay.switchVisibilityBy(loading)
                }
            }
        }
    }
    private fun FragmentDetailBinding.loadSimilarItemsByApi() {
        viewModel.getSimilarByApi(recipeId)
        viewModel.latestSimilarData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseWrapper.Loading -> {
                    similarList.showShimmer()
                }

                is ResponseWrapper.Success -> {
                    it.data?.let { responseSimilar ->
                        similarList.hideShimmer()
                        fillSimilarList(responseSimilar)
                    }
                }

                is ResponseWrapper.Error -> {
                    similarList.hideShimmer()
                }
            }
        }
    }

    private fun checkForCachedDetail() {
        viewModel.checkForDetailExistence(recipeId)
        viewModel.isThisDetailEntityExist.observe(viewLifecycleOwner) {
            isCached = it
            Timber.d("is Cached: $isCached")
        }
    }

    private fun loadCachedDetail() {
        viewModel.getLocalDetailData(recipeId).observe(viewLifecycleOwner) {
            binding.fillViews(it.result)
            binding.contentLay.isVisible = true
            Timber.d("Result ID: ${it.result.id}")
        }
    }

    private fun fillSimilarList(similarItems: ResponseSimilar)  {
        similarAdapter.setData(similarItems)
        binding.similarList.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            similarAdapter
        )
        //Click
        similarAdapter.setOnItemClickListener {
            val action = DetailFragmentDirections.actionToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentDetailBinding.fillViews(data: ResponseDetail) {
        //Image
        val imageSplit = data.image!!.split("-")
        val imageSize = imageSplit[1].replace(Constants.OLD_IMAGE_SIZE, Constants.NEW_IMAGE_SIZE)
        coverImg.load("${imageSplit[0]}-$imageSize") {
            crossfade(true)
            crossfade(800)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_placeholder)
        }
        // Favorites
        favoriteImg.setOnClickListener {
            root.showSnackBar("Clicked: $isFromFavorite")
            if (isFromFavorite) removeFromFavorites(data.id!!, data) else saveToFavorites(data.id!!, data)
        }
        checkForFavoriteExistence()
        //Source
        data.sourceUrl?.let { source ->
            sourceImg.isVisible = true
            sourceImg.setOnClickListener {
                val direction = DetailFragmentDirections.actionToWebViewFragment(source)
                findNavController().navigate(direction)
            }
        }
        //Text
        timeTxt.text = data.readyInMinutes!!.convertMinToHour()
        nameTxt.text = data.title
        //Desc
        val summary = HtmlCompat.fromHtml(data.summary!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
        descTxt.text = summary
        //Toggle
        if (data.cheap!!) cheapTxt.setDynamicallyColor(R.color.caribbean_green)
        if (data.veryPopular!!) popularTxt.setDynamicallyColor(R.color.tart_orange)
        if (data.vegan!!) veganTxt.setDynamicallyColor(R.color.caribbean_green)
        if (data.dairyFree!!) dairyTxt.setDynamicallyColor(R.color.caribbean_green)
        //Like
        likeTxt.text = data.aggregateLikes.toString()
        //price
        priceTxt.text = "${data.pricePerServing} $"
        //Healthy
        healthyTxt.text = data.healthScore.toString()
        when (data.healthScore!!.toInt()) {
            in 90..100 -> healthyTxt.setDynamicallyColor(R.color.caribbean_green)
            in 60..89 -> healthyTxt.setDynamicallyColor(R.color.chineseYellow)
            in 0..59 -> healthyTxt.setDynamicallyColor(R.color.tart_orange)
        }
        //Instructions
        instructionsCount.text = "${data.extendedIngredients!!.size} ${getString(R.string.items)}"
        val instructions =
            HtmlCompat.fromHtml(data.instructions!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
        instructionsDesc.text = instructions
        initInstructionsList(data.extendedIngredients)
        //Steps
        initStepsList(data.analyzedInstructions!![0].steps!!)
        stepsShowMore.setOnClickListener {
            val direction = DetailFragmentDirections.actionDetailFragmentToStepsFragment2(data.analyzedInstructions[0])
            findNavController().navigate(direction)
        }
        //Diets
        setupChip(data.diets!!, dietsChipGroup)
    }

    // Favorites OPs
    private fun FragmentDetailBinding.checkForFavoriteExistence() {
        viewModel.existsFavoriteData.observe(viewLifecycleOwner) {
            isFromFavorite = it
            if (it) {
                favoriteImg.apply {
                    setTint(R.color.tart_orange)
                    setImageResource(R.drawable.ic_heart_circle_minus)
                }
            } else {
                favoriteImg.apply {
                    setTint(R.color.persianGreen)
                    setImageResource(R.drawable.ic_heart_circle_plus)
                }
            }
        }
    }
    private fun saveToFavorites(id: Int, result: ResponseDetail) {
        val entity = FavoriteEntity(id, result)
        viewModel.saveFavorite(entity)
    }
    private fun removeFromFavorites(id: Int, result: ResponseDetail) {
        val entity = FavoriteEntity(id, result)
        viewModel.deleteFavorite(entity)
    }

    private fun initInstructionsList(list: List<ResponseDetail.ExtendedIngredient>) {
        if (list.isNotEmpty()) {
            instructionsAdapter.setData(list)
            binding.instructionsList.setupRecyclerview(
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                instructionsAdapter
            )
        }
    }

    private fun initStepsList(list: List<ResponseDetail.AnalyzedInstruction.Step>) {
        if (list.isNotEmpty()) {
            Constants.STEPS_COUNT = if (list.size < 3) {
                list.size
            } else {
                3
            }
            stepsAdapter.setData(list)
            binding.apply {
                stepsList.setupRecyclerview(LinearLayoutManager(requireContext()), stepsAdapter)
                //Show more
                if (list.size > 3) {
                    stepsShowMore.isVisible = true
                }
            }
        }
    }

    private fun setupChip(list: List<String>, view: ChipGroup) {
        list.forEach {
            val chip = Chip(requireContext())
            val drawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.DietsChip)
            chip.setChipDrawable(drawable)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkGray))
            chip.text = it
            view.addView(chip)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
package ir.mahan.wikifoodia.ui.details

import ir.mahan.wikifoodia.adapter.InstructionsAdapter
import ir.mahan.wikifoodia.adapter.StepsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import ir.mahan.wikifoodia.databinding.FragmentDetailBinding
import ir.mahan.wikifoodia.models.detail.ResponseDetail
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.convertMinToHour
import ir.mahan.wikifoodia.utils.setDynamicallyColor
import ir.mahan.wikifoodia.utils.setupRecyclerview
import ir.mahan.wikifoodia.utils.switchVisibilityBy
import ir.mahan.wikifoodia.viewmodels.DetailViewmodel
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : Fragment() {

    // binding object
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var instructionsAdapter: InstructionsAdapter
    @Inject lateinit var stepsAdapter: StepsAdapter

    //ViewModel
    private val viewModel: DetailViewmodel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var recipeId = 0


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
            if (recipeId > 0) viewModel.getFoodDetailsByApi(it.foodID)
        }
        binding.apply {
            //Back
            backImg.setOnClickListener { findNavController().popBackStack() }
            // Details
            viewModel.LatestDetailData.observe(viewLifecycleOwner) {
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
    }

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
        //Source
        data.sourceUrl?.let { source ->
            sourceImg.isVisible = true
            sourceImg.setOnClickListener {
                // Todo: Open Source
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

        }
        //Diets
        setupChip(data.diets!!, dietsChipGroup)
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
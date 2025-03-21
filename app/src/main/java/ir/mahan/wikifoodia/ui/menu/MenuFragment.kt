package ir.mahan.wikifoodia.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.FragmentMenuBinding
import ir.mahan.wikifoodia.utils.observeOnce
import ir.mahan.wikifoodia.viewmodels.MenuViewModel

@AndroidEntryPoint
class MenuFragment : BottomSheetDialogFragment() {


    // binding object
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuViewModel: MenuViewModel
    private var chipCounter = 1
    private var chipMealTitle = ""
    private var chipMealId = 0
    private var chipDietTitle = ""
    private var chipDietId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuViewModel = ViewModelProvider(requireActivity())[MenuViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Setup Chips
            initializeChipGroup(menuViewModel.mealTypes, mealChipGroup)
            initializeChipGroup(menuViewModel.dietTypes, dietChipGroup)
            // Getting Menu Filters latest States
            menuViewModel.storedMenuFilters.observeOnce(viewLifecycleOwner) {
                chipMealTitle = it.meal
                chipDietTitle = it.diet
                updateChip(it.mealId, mealChipGroup)
                updateChip(it.dietId, dietChipGroup)
            }
            // Handle Meal ChipGroups Click
            mealChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                var chip: Chip
                checkedIds.forEach {
                    chip = group.findViewById(it)
                    chipMealTitle = chip.text.toString().lowercase()
                    chipMealId = it
                }
            }
            // Handle Diet ChipGroups Click
            dietChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                var chip: Chip
                checkedIds.forEach {
                    chip = group.findViewById(it)
                    chipDietTitle = chip.text.toString().lowercase()
                    chipDietId = it
                }
            }
            //Submit
            submitBtn.setOnClickListener {
                menuViewModel.saveMenuData(chipMealTitle, chipMealId, chipDietTitle, chipDietId)
                findNavController().navigate(MenuFragmentDirections.actionMenuToRecipe().setShouldUpdate(true))
            }
        }
    }

    // Utility Functions
    private fun updateChip(id: Int, view: ChipGroup) {
        if (id != 0) {
            view.findViewById<Chip>(id).isChecked = true
        }
    }

    private fun initializeChipGroup(items: List<String>, chipGroup: ChipGroup) {
        items.forEachIndexed { index, item ->
            val chip = Chip(requireContext())
            val drawable =
                ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.DarkChip)
            chip.setChipDrawable(drawable)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            chip.id = chipCounter++
            chip.text = item
            chipGroup.addView(chip)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
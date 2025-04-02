package ir.mahan.wikifoodia.ui.steps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.adapter.StepsAdapter
import ir.mahan.wikifoodia.databinding.FragmentLuckyBinding
import ir.mahan.wikifoodia.databinding.FragmentStepsBinding
import ir.mahan.wikifoodia.models.detail.ResponseDetail
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.setupRecyclerview
import javax.inject.Inject

@AndroidEntryPoint
class StepsFragment : BottomSheetDialogFragment() {
    // binding object
    private var _binding: FragmentStepsBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var stepsAdapter: StepsAdapter

    private val args: StepsFragmentArgs by navArgs()
    private lateinit var steps: List<ResponseDetail.AnalyzedInstruction.Step>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStepsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            args.let {
                steps =it.data.steps!!
                Constants.STEPS_COUNT = steps.size
                stepsAdapter.setData(steps)
                stepsList.setupRecyclerview(LinearLayoutManager(requireContext()), stepsAdapter)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package ir.mahan.wikifoodia.ui.lucky

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.FragmentFavoritesBinding
import ir.mahan.wikifoodia.databinding.FragmentLuckyBinding


class LuckyFragment : Fragment() {
    // binding object
    private var _binding: FragmentLuckyBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLuckyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {}
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
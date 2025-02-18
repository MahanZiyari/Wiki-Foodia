package ir.mahan.wikifoodia.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.FragmentFavoritesBinding
import ir.mahan.wikifoodia.databinding.FragmentSplashBinding
import ir.mahan.wikifoodia.viewmodels.RegisterViewModel


class FavoritesFragment : Fragment() {
    // binding object
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
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
package ir.mahan.wikifoodia.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.adapter.FavoriteAdapter
import ir.mahan.wikifoodia.databinding.FragmentFavoritesBinding
import ir.mahan.wikifoodia.databinding.FragmentSplashBinding
import ir.mahan.wikifoodia.utils.setupRecyclerview
import ir.mahan.wikifoodia.utils.switchVisibilityBy
import ir.mahan.wikifoodia.viewmodels.FavoritesViewModel
import ir.mahan.wikifoodia.viewmodels.RegisterViewModel
import javax.inject.Inject


@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    // binding object
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var favoriteAdapter: FavoriteAdapter

    private val viewModel: FavoritesViewModel by viewModels()


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
        binding.apply {
            //Load favorites
            viewModel.favoritesItems.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    emptyTxt.switchVisibilityBy(favoriteList)
                    //Data
                    favoriteAdapter.setData(it)
                    favoriteList.setupRecyclerview(LinearLayoutManager(requireContext()), favoriteAdapter)
                    //Click
                    favoriteAdapter.setOnItemClickListener { id ->
                        val action = FavoritesFragmentDirections.actionToDetailsFragment(id)
                        findNavController().navigate(action)
                    }
                } else {
                    favoriteList.isVisible =  false
                    emptyTxt.isVisible = true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
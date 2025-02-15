package ir.mahan.wikifoodia.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.FragmentSplashBinding
import ir.mahan.wikifoodia.viewmodels.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment() {

    // binding object
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Load background cover
            backgroundImg.load(R.drawable.bg_splash)
            //Application Version
            versionTxt.text = "${getString(R.string.version)} : 99.99"
            // Handle after navigation
            lifecycleScope.launch {
                delay(2500)
                findNavController().run {
                    popBackStack(R.id.splashFragment, true)
                    registerViewModel.datastoreRegisterData.asLiveData().observe(viewLifecycleOwner) {
                        Timber.d("DataStore: $it")
                        if (it.username.isNotEmpty() && it.hash.isNotEmpty()) {
                            Timber.d("Registered")
                            navigate(R.id.action_to_recipeFragment)
                        } else {
                            Timber.d("Not Registered")
                            navigate(R.id.action_to_registerFragment)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
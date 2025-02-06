package ir.mahan.wikifoodia.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    // binding object
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            coverImg.load(R.drawable.register_logo)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
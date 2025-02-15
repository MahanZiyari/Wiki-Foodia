package ir.mahan.wikifoodia.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.FragmentRegisterBinding
import ir.mahan.wikifoodia.utils.NetworkChecker
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.showSnackBar
import ir.mahan.wikifoodia.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    // binding object
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodyRegister
    @Inject
    lateinit var networkChecker: NetworkChecker
    // Other
    private val viewModel: RegisterViewModel by viewModels()
    private var firstName = ""
    private var lastName = ""
    private var userName = ""
    private var email = ""


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
            // Fields  checking
            fNameEdt.addTextChangedListener{
                if (it.toString().contains(regex = Regex("^[a-zA-Z]+\$"))){
                    firstName = it.toString()
                    fNameInputLay.error = ""
                } else {
                    fNameInputLay.error = getString(R.string.notValidName)
                }
            }
            lastNameEdt.addTextChangedListener{
                if (it.toString().contains(regex = Regex("^[a-zA-Z]+\$"))){
                    lastName = it.toString()
                    lastNameInputLay.error = ""
                } else {
                    lastNameInputLay.error = getString(R.string.notValidName)
                }
            }
            userNameEdt.addTextChangedListener{
                if (it.toString().contains(regex = Regex("^[^\\s\\W_]+\$"))){
                    userName = it.toString()
                    userNameInputLay.error = ""
                } else {
                    userNameInputLay.error = getString(R.string.notValidUserName)
                }
            }
            emailEdt.addTextChangedListener{
                if (it.toString().contains(regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"))){
                    email = it.toString()
                    emailInputLay.error = ""
                } else {
                    emailInputLay.error = getString(R.string.emailNotValid)
                }
            }
            // Register button
            registerBtn.setOnClickListener {
                body.firstName = firstName
                body.lastName = lastName
                body.username = userName
                body.email = email
                lifecycleScope.launch {
                    networkChecker.observeNetworkState().collect{ state ->
                        if (state == true) {
                            viewModel.callRegisterApi(body)
                        } else {
                            root.showSnackBar("Check your internet connection")
                        }
                    }
                }
            }
            // Loading Data
            loadRegisterResult()
        }
    }

    private fun loadRegisterResult() {
        viewModel.registerData.observe(viewLifecycleOwner) { wrappedResult  ->
            when (wrappedResult) {
                is ResponseWrapper.Loading -> {}
                is ResponseWrapper.Success -> {
                    wrappedResult.data?.let {
                        viewModel.saveRegisterData(
                            username = it.username.toString(),
                            hash = it.hash.toString()
                        )
                        findNavController().run {
                            Timber.d("Inside NavController")
                            popBackStack(R.id.registerFragment, true)
                            navigate(R.id.action_to_recipeFragment)
                        }
                    }
                }
                is ResponseWrapper.Error -> {
                    binding.root.showSnackBar(wrappedResult.message!!)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
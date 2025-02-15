package ir.mahan.wikifoodia.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.repository.RegisterRepository
import ir.mahan.wikifoodia.models.register.ResponseRegister
import ir.mahan.wikifoodia.ui.register.BodyRegister
import ir.mahan.wikifoodia.utils.ResponseHandler
import ir.mahan.wikifoodia.utils.ResponseWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository) : ViewModel()  {

    val registerData = MutableLiveData<ResponseWrapper<ResponseRegister>>()

    fun callRegisterApi(body: BodyRegister) = viewModelScope.launch {
        registerData.value = ResponseWrapper.Loading()
        val apiResponse = repository.postRegisterUser(body)
        registerData.value = ResponseHandler(apiResponse).generalNetworkResponse()
    }

    fun saveRegisterData(username: String, hash: String) = viewModelScope.launch {
        repository.saveRegisterData(username, hash)
    }

    val datastoreRegisterData = repository.registerData
}
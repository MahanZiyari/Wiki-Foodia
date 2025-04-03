package ir.mahan.wikifoodia.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.repository.DetailRepository
import ir.mahan.wikifoodia.models.detail.ResponseDetail
import ir.mahan.wikifoodia.models.detail.ResponseSimilar
import ir.mahan.wikifoodia.utils.ResponseHandler
import ir.mahan.wikifoodia.utils.ResponseWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewmodel @Inject constructor(private val repository: DetailRepository) : ViewModel() {

    //Api: Details
    val latestDetailData = MutableLiveData<ResponseWrapper<ResponseDetail>>()
    fun getFoodDetailsByApi(foodId: Int) = viewModelScope.launch {
        latestDetailData.value = ResponseWrapper.Loading()
        val response = repository.getFoodDetailByID(foodId)
        latestDetailData.value = ResponseHandler(response).generalNetworkResponse()
    }
    //Api: Similar
    val latestSimilarData = MutableLiveData<ResponseWrapper<ResponseSimilar>>()
    fun getSimilarByApi(foodId: Int) = viewModelScope.launch {
        latestSimilarData.value = ResponseWrapper.Loading()
        val response = repository.getSimilarItemsById(foodId)
        latestSimilarData.value = ResponseHandler(response).generalNetworkResponse()
    }
}
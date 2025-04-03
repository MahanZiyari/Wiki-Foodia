package ir.mahan.wikifoodia.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.database.entity.DetailEntity
import ir.mahan.wikifoodia.data.database.entity.FavoriteEntity
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
        // Cache
        val cachedData = latestDetailData.value?.data
        if (cachedData != null) {
            cacheDetail(cachedData.id!!, cachedData)
        }
    }
    // Local: Detail
    val isThisDetailEntityExist = MutableLiveData<Boolean>()
    fun checkForDetailExistence(id: Int) = viewModelScope.launch {
        repository.checkDetailEntityExistence(id).collect { isThisDetailEntityExist.postValue(it) }
    }

    fun saveDetail(detailEntity: DetailEntity) = viewModelScope.launch {
        repository.saveDetail(detailEntity)
    }

    fun getLocalDetailData(id: Int) = repository.getDetail(id).asLiveData()

    private fun cacheDetail(id: Int, response: ResponseDetail) {
        val entity = DetailEntity(id, response)
        saveDetail(entity)
    }
    //Api: Similar
    val latestSimilarData = MutableLiveData<ResponseWrapper<ResponseSimilar>>()
    fun getSimilarByApi(foodId: Int) = viewModelScope.launch {
        latestSimilarData.value = ResponseWrapper.Loading()
        val response = repository.getSimilarItemsById(foodId)
        latestSimilarData.value = ResponseHandler(response).generalNetworkResponse()
    }

    //Favorite
    fun saveFavorite(entity: FavoriteEntity) = viewModelScope.launch {
        repository.saveFavorite(entity)
    }

    fun deleteFavorite(entity: FavoriteEntity) = viewModelScope.launch {
        repository.deleteFavorite(entity)
    }

    val existsFavoriteData = MutableLiveData<Boolean>()
    fun checkForFavoriteExistence(id: Int) = viewModelScope.launch {
        repository.checkFavoriteEntityExistence(id).collect { existsFavoriteData.postValue(it) }
    }
}
package ir.mahan.wikifoodia.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.repository.RecipesRepository
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.ResponseHandler
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.constants.APIParameters
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipesViewmodel @Inject constructor(private val repository: RecipesRepository): ViewModel() {

    //---Popular---//
    //Queries
    fun popularQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[APIParameters.API_KEY] = Constants.MY_API_KEY
        queries[APIParameters.NUMBER] = APIParameters.LIMITED_COUNT.toString()
        queries[APIParameters.SORT] = APIParameters.SORT_POPULARITY
        queries[APIParameters.ADD_RECIPE_INFORMATION] = APIParameters.TRUE
        return queries
    }
    //Api
    val popularData = MutableLiveData<ResponseWrapper<ResponseRecipes>>()
    fun callPopularApi(queries: Map<String, String>) = viewModelScope.launch {
        popularData.value = ResponseWrapper.Loading()
        val response = repository.remote.searchRecipes(queries)
        popularData.value = ResponseHandler(response).generalNetworkResponse()
    }

    //---Recent---//
    //Queries
    fun recentQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[APIParameters.API_KEY] = Constants.MY_API_KEY
        queries[APIParameters.NUMBER] = APIParameters.FULL_COUNT.toString()
        queries[APIParameters.TYPE] = APIParameters.TYPE_MAIN_COURSE
        queries[APIParameters.DIET] = APIParameters.DIET_GLUTEN_FREE
        queries[APIParameters.ADD_RECIPE_INFORMATION] = APIParameters.TRUE
        return queries
    }
    //Api
    val recentData = MutableLiveData<ResponseWrapper<ResponseRecipes>>()
    fun callRecentApi(queries: Map<String, String>) = viewModelScope.launch {
        Timber.d("ViewModel: calling Recent API")
        recentData.value = ResponseWrapper.Loading()
        val response = repository.remote.searchRecipes(queries)
        Timber.d("Response Code: ${response.code()}")
        recentData.value = checkResponseResult(response)
    }

    private fun checkResponseResult(response: Response<ResponseRecipes>): ResponseWrapper<ResponseRecipes> {
        return when {
            response.message().contains("timeout") -> ResponseWrapper.Error("Timeout")
            response.code() == 401 -> ResponseWrapper.Error("You are not authorized")
            response.code() == 402 -> ResponseWrapper.Error("Your free plan finished")
            response.code() == 422 -> ResponseWrapper.Error("Api key not found!")
            response.code() == 500 -> ResponseWrapper.Error("Try again")
            response.body()!!.results.isNullOrEmpty() -> ResponseWrapper.Error("Not found any recipe!")
            response.isSuccessful -> ResponseWrapper.Success(response.body()!!)
            else -> ResponseWrapper.Error(response.message())
        }
    }
}
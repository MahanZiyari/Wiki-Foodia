package ir.mahan.wikifoodia.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.database.RecipeEntity
import ir.mahan.wikifoodia.data.repository.MenuRepository
import ir.mahan.wikifoodia.data.repository.RecipesRepository
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.ResponseHandler
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.constants.APIParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipesViewmodel @Inject constructor(private val repository: RecipesRepository, private val menuRepository: MenuRepository): ViewModel() {



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
        val cachedResponse = popularData.value?.data
        cachedResponse?.let {
            cachePopularFoods(it)
        }
    }

    // Local
    val  popularFoodsFromDB = repository.local.getAllRecipes().asLiveData()
    private fun cachePopularFoods(response: ResponseRecipes) {
        val entity = RecipeEntity(
            id = 0,
            responseRecipes = response
        )
        savePopularFood(entity)
    }
    private fun savePopularFood(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipe(entity)
    }

    //---Recent---//
    private var mealType = APIParameters.TYPE_MAIN_COURSE
    private var dietType = APIParameters.DIET_GLUTEN_FREE
    //Queries
    fun recentQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            menuRepository.menuFilters.collect {
                mealType = it.meal
                dietType = it.diet
                Timber.d("inside collect: $mealType, $dietType")
                queries[APIParameters.API_KEY] = Constants.MY_API_KEY
                queries[APIParameters.NUMBER] = APIParameters.FULL_COUNT.toString()
                queries[APIParameters.TYPE] = mealType
                queries[APIParameters.DIET] = dietType
                queries[APIParameters.ADD_RECIPE_INFORMATION] = APIParameters.TRUE
                Timber.d("Before Return: $mealType, $dietType")
            }
        }
        return queries
    }
    //Api
    val recentData = MutableLiveData<ResponseWrapper<ResponseRecipes>>()
    fun callRecentApi(queries: Map<String, String>) = viewModelScope.launch {
        recentData.value = ResponseWrapper.Loading()
        val response = repository.remote.searchRecipes(queries)
        recentData.value = checkResponseResult(response)
        val cachedResponse = recentData.value?.data
        cachedResponse?.let {
            cacheRecentFoods(it)
        }
    }

    // Local
    val  recentFoodsFromDB = repository.local.getAllRecipes().asLiveData()
    private fun cacheRecentFoods(response: ResponseRecipes) {
        val entity = RecipeEntity(
            id = 1,
            responseRecipes = response
        )
        saveRecentFood(entity)
    }
    private fun saveRecentFood(entity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.saveRecipe(entity)
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
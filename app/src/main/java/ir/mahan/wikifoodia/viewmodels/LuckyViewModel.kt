package ir.mahan.wikifoodia.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.repository.LuckyRepository
import ir.mahan.wikifoodia.models.lucky.ResponseLucky
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.utils.ResponseHandler
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.constants.APIParameters
import ir.mahan.wikifoodia.utils.constants.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LuckyViewModel @Inject constructor(private val repository: LuckyRepository) : ViewModel() {

    fun luckyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[APIParameters.API_KEY] = Constants.MY_API_KEY
        queries[APIParameters.NUMBER] = "1"
        queries[APIParameters.ADD_RECIPE_INFORMATION] = APIParameters.TRUE
        return queries
    }
    //Api
    val randomRecipe = MutableLiveData<ResponseWrapper<ResponseLucky>>()
    fun getRandomRecipe(queries: Map<String, String>) = viewModelScope.launch {
        randomRecipe.value = ResponseWrapper.Loading()
        val response = repository.getRandomRecipe(queries)
        randomRecipe.value = ResponseHandler(response).generalNetworkResponse()
    }
}
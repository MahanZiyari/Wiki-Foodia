package ir.mahan.wikifoodia.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.repository.LuckyRepository
import ir.mahan.wikifoodia.data.repository.SearchRepository
import ir.mahan.wikifoodia.models.lucky.ResponseLucky
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.utils.ResponseHandler
import ir.mahan.wikifoodia.utils.ResponseWrapper
import ir.mahan.wikifoodia.utils.constants.APIParameters
import ir.mahan.wikifoodia.utils.constants.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {

    fun searchQueries(searchTerm: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[APIParameters.API_KEY] = Constants.MY_API_KEY
        queries[APIParameters.NUMBER] = APIParameters.FULL_COUNT.toString()
        queries[APIParameters.ADD_RECIPE_INFORMATION] = APIParameters.TRUE
        queries[APIParameters.QUERY] = searchTerm
        return queries
    }
    //Api
    val searchResult = MutableLiveData<ResponseWrapper<ResponseRecipes>>()
    fun searchRecipes(queries: Map<String, String>) = viewModelScope.launch {
        searchResult.value = ResponseWrapper.Loading()
        val response = repository.searchRecipes(queries)
        searchResult.value = ResponseHandler(response).generalNetworkResponse()
    }
}
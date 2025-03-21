package ir.mahan.wikifoodia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mahan.wikifoodia.data.repository.MenuRepository
import ir.mahan.wikifoodia.utils.constants.APIParameters
import ir.mahan.wikifoodia.utils.uppercaseFirstLetter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val repository: MenuRepository): ViewModel() {

    val mealTypes = buildList{
        add(APIParameters.TYPE_MAIN_COURSE.uppercaseFirstLetter())
        add(APIParameters.TYPE_FINGERFOOD.uppercaseFirstLetter())
        add(APIParameters.TYPE_SIDE_DISH.uppercaseFirstLetter())
        add(APIParameters.TYPE_APPETIZER.uppercaseFirstLetter())
        add(APIParameters.TYPE_BREAKFAST.uppercaseFirstLetter())
        add(APIParameters.TYPE_BREAD.uppercaseFirstLetter())
        add(APIParameters.TYPE_DRINK.uppercaseFirstLetter())
        add(APIParameters.TYPE_SOUP.uppercaseFirstLetter())
        add(APIParameters.TYPE_SALAD.uppercaseFirstLetter())
        add(APIParameters.TYPE_SAUCE.uppercaseFirstLetter())
        add(APIParameters.TYPE_SNACK.uppercaseFirstLetter())
        add(APIParameters.TYPE_DESSERT.uppercaseFirstLetter())
        add(APIParameters.TYPE_BEVERAGE.uppercaseFirstLetter())
        add(APIParameters.TYPE_MARINADE.uppercaseFirstLetter())
    }

    val dietTypes = buildList{
        add(APIParameters.DIET_KETOGENIC.uppercaseFirstLetter())
        add(APIParameters.DIET_VEGETARIAN.uppercaseFirstLetter())
        add(APIParameters.DIET_PALEO.uppercaseFirstLetter())
        add(APIParameters.DIET_VEGAN.uppercaseFirstLetter())
        add(APIParameters.DIET_PRIMAL.uppercaseFirstLetter())
        add(APIParameters.DIET_GLUTEN_FREE.uppercaseFirstLetter())
        add(APIParameters.DIET_PESCETARIAN.uppercaseFirstLetter())
        add(APIParameters.DIET_WHOLE30.uppercaseFirstLetter())
        add(APIParameters.DIET_LACTO_VEGETARIAN.uppercaseFirstLetter())
        add(APIParameters.DIET_LOW_FODMAP.uppercaseFirstLetter())
        add(APIParameters.DIET_OVO_VEGETARIAN.uppercaseFirstLetter())
    }

    fun saveMenuData(meal: String, mealId: Int, diet: String, dietId: Int) = viewModelScope.launch {
        repository.saveMenuFilters(meal, mealId, diet, dietId)
    }

    val storedMenuFilters = repository.menuFilters.asLiveData()
}
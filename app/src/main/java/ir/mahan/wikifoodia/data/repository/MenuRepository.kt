package ir.mahan.wikifoodia.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ir.mahan.wikifoodia.models.menu.MenuStoredModel
import ir.mahan.wikifoodia.utils.constants.APIParameters
import ir.mahan.wikifoodia.utils.constants.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class MenuRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object StoredKeys{
        val selectedMealType = stringPreferencesKey(Constants.MENU_MEAL_TITLE_KEY)
        val selectedMealId = intPreferencesKey(Constants.MENU_MEAL_ID_KEY)
        val selectedDietType = stringPreferencesKey(Constants.MENU_DIET_TITLE_KEY)
        val selectedDietId = intPreferencesKey(Constants.MENU_DIET_ID_KEY)
    }

    private val Context.dataSore: DataStore<Preferences> by preferencesDataStore(Constants.MENU_DATASTORE)

    suspend fun saveMenuFilters(meal: String, mealId: Int, diet: String, dietId: Int) {
        context.dataSore.edit {
            it[StoredKeys.selectedMealType] = meal
            it[StoredKeys.selectedMealId] = mealId
            it[StoredKeys.selectedDietType] = diet
            it[StoredKeys.selectedDietId] = dietId
        }
    }

    val menuFilters: Flow<MenuStoredModel> = context.dataSore.data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        val selectMeal = it[StoredKeys.selectedMealType] ?: APIParameters.TYPE_MAIN_COURSE
        val selectMealId = it[StoredKeys.selectedMealId] ?: 0
        val selectDiet = it[StoredKeys.selectedDietType] ?: APIParameters.DIET_GLUTEN_FREE
        val selectDietId = it[StoredKeys.selectedDietId] ?: 0
        MenuStoredModel(selectMeal, selectMealId, selectDiet, selectDietId)
    }
}
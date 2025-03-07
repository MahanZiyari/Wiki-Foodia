package ir.mahan.wikifoodia.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.mahan.wikifoodia.utils.constants.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM ${Constants.RECIPE_TABLE_NAME} ORDER BY ID ASC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>
}
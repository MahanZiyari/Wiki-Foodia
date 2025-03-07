package ir.mahan.wikifoodia.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.utils.constants.Constants

@Entity(tableName = Constants.RECIPE_TABLE_NAME)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var responseRecipes: ResponseRecipes
)

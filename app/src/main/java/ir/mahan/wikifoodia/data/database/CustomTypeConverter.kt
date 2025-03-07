package ir.mahan.wikifoodia.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes

class CustomTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun recipeToJson(recipe: ResponseRecipes): String {
        return gson.toJson(recipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String): ResponseRecipes {
        return gson.fromJson(data, ResponseRecipes::class.java)
    }

}
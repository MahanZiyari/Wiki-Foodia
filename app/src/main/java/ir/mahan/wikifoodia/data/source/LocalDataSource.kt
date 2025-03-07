package ir.mahan.wikifoodia.data.source

import ir.mahan.wikifoodia.data.database.RecipeAppDao
import ir.mahan.wikifoodia.data.database.RecipeEntity
import ir.mahan.wikifoodia.data.network.ApiServices
import ir.mahan.wikifoodia.ui.register.BodyRegister
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipeAppDao) {
    suspend fun saveRecipe(entity: RecipeEntity) = dao.insertRecipe(entity)
    fun getAllRecipes() = dao.getAllRecipes()
}
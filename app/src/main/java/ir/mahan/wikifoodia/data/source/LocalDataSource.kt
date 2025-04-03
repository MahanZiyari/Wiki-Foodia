package ir.mahan.wikifoodia.data.source

import ir.mahan.wikifoodia.data.database.RecipeAppDao
import ir.mahan.wikifoodia.data.database.entity.DetailEntity
import ir.mahan.wikifoodia.data.database.entity.RecipeEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipeAppDao) {
    suspend fun saveRecipe(entity: RecipeEntity) = dao.insertRecipe(entity)
    fun getAllRecipes() = dao.getAllRecipes()
    //Detail
    suspend fun saveDetail(entity: DetailEntity) = dao.saveDetail(entity)
    fun loadDetail(id: Int) = dao.loadDetail(id)
    fun existsDetail(id: Int) = dao.existsDetail(id)
}
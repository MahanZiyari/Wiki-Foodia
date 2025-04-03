package ir.mahan.wikifoodia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.mahan.wikifoodia.data.database.entity.DetailEntity
import ir.mahan.wikifoodia.data.database.entity.FavoriteEntity
import ir.mahan.wikifoodia.data.database.entity.RecipeEntity
import ir.mahan.wikifoodia.utils.constants.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM ${Constants.RECIPE_TABLE_NAME} ORDER BY ID ASC")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    //Detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDetail(entity: DetailEntity)

    @Query("SELECT * FROM ${Constants.DETAIL_TABLE_NAME} WHERE id = :id")
    fun loadDetail(id: Int): Flow<DetailEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM ${Constants.DETAIL_TABLE_NAME} WHERE ID = :id)")
    fun existsDetail(id: Int): Flow<Boolean>

    // Favorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: FavoriteEntity)

    @Query("SELECT * FROM ${Constants.FAVORITE_TABLE_NAME} ORDER BY ID ASC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavorite(entity: FavoriteEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM ${Constants.FAVORITE_TABLE_NAME} WHERE ID = :id)")
    fun existsFavorite(id: Int): Flow<Boolean>
}
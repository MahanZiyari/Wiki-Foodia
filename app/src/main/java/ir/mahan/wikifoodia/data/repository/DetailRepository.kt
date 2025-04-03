package ir.mahan.wikifoodia.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ir.mahan.wikifoodia.data.database.entity.DetailEntity
import ir.mahan.wikifoodia.data.database.entity.FavoriteEntity
import ir.mahan.wikifoodia.data.source.LocalDataSource
import ir.mahan.wikifoodia.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class DetailRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    suspend fun getFoodDetailByID(id: Int) = remoteDataSource.getFoodDetailById(id)
    suspend fun getSimilarItemsById(id: Int) = remoteDataSource.getSimilarItemsById(id)

    //  Detail: Local
    suspend fun saveDetail(detailEntity: DetailEntity) = localDataSource.saveDetail(detailEntity)
    fun getDetail(id: Int) = localDataSource.loadDetail(id)
    fun checkDetailEntityExistence(id: Int) = localDataSource.existsDetail(id)

    // Favorites
    suspend fun saveFavorite(entity: FavoriteEntity) = localDataSource.insertFavorite(entity)
    suspend fun deleteFavorite(entity: FavoriteEntity) = localDataSource.deleteFavorite(entity)
    fun getAllFavorites() = localDataSource.getAllFavorites()
    fun checkFavoriteEntityExistence(id: Int) = localDataSource.existsFavorite(id)

}
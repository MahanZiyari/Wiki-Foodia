package ir.mahan.wikifoodia.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ir.mahan.wikifoodia.data.database.entity.DetailEntity
import ir.mahan.wikifoodia.data.database.entity.FavoriteEntity
import ir.mahan.wikifoodia.data.source.LocalDataSource
import ir.mahan.wikifoodia.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class FavoritesRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    fun getAllFavorites() = localDataSource.getAllFavorites()

}
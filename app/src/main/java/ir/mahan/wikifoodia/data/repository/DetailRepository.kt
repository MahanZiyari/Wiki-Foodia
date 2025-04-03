package ir.mahan.wikifoodia.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
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
}
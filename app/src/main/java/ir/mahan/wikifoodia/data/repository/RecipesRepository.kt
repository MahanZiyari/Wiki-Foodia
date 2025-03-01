package ir.mahan.wikifoodia.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ir.mahan.wikifoodia.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class RecipesRepository @Inject constructor(remoteDataSource: RemoteDataSource) {

    val remote = remoteDataSource
}
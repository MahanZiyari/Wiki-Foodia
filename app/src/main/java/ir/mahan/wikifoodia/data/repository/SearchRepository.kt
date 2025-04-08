package ir.mahan.wikifoodia.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ir.mahan.wikifoodia.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class SearchRepository @Inject constructor(private val remote: RemoteDataSource) {
    suspend fun searchRecipes(queries: Map<String, String>) = remote.searchRecipes(queries)
}
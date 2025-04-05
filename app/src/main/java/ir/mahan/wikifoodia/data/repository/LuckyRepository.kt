package ir.mahan.wikifoodia.data.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ir.mahan.wikifoodia.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class LuckyRepository @Inject constructor(private val remote: RemoteDataSource) {
    suspend fun getRandomRecipe(queries: Map<String, String>) = remote.getRandomRecipe(queries)
}
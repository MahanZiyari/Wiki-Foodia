package ir.mahan.wikifoodia.data.source

import ir.mahan.wikifoodia.data.network.ApiServices
import ir.mahan.wikifoodia.ui.register.BodyRegister
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: ApiServices) {
    suspend fun postRegisterUser(body: BodyRegister) = api.postRegisterUser(body = body)
    suspend fun searchRecipes(queries: Map<String, String>) = api.searchRecipes(queries)
}
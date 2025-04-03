package ir.mahan.wikifoodia.data.network

import ir.mahan.wikifoodia.models.detail.ResponseDetail
import ir.mahan.wikifoodia.models.detail.ResponseSimilar
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes
import ir.mahan.wikifoodia.models.register.ResponseRegister
import ir.mahan.wikifoodia.ui.register.BodyRegister
import ir.mahan.wikifoodia.utils.constants.APIParameters.API_KEY
import ir.mahan.wikifoodia.utils.constants.Constants.MY_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiServices {
    @POST("users/connect")
    suspend fun postRegisterUser(
        @Query(API_KEY) apiKey: String = MY_API_KEY,
        @Body body: BodyRegister
    ): Response<ResponseRegister>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(@QueryMap queries: Map<String, String>): Response<ResponseRecipes>

    @GET("recipes/{id}/information")
    suspend fun getFoodDetailById(@Path("id") id: Int, @Query(API_KEY) apiKey: String = MY_API_KEY): Response<ResponseDetail>

    @GET("recipes/{id}/similar")
    suspend fun getSimilarItemsById(@Path("id") id: Int, @Query(API_KEY) apiKey: String = MY_API_KEY): Response<ResponseSimilar>
}
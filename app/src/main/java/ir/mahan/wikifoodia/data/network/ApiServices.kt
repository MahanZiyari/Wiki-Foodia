package ir.mahan.wikifoodia.data.network

import ir.mahan.wikifoodia.models.register.ResponseRegister
import ir.mahan.wikifoodia.ui.register.BodyRegister
import ir.mahan.wikifoodia.utils.Constants.API_KEY
import ir.mahan.wikifoodia.utils.Constants.MY_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {
    @POST("users/connect")
    suspend fun postRegisterUser(
        @Query(API_KEY) apiKey: String = MY_API_KEY,
        @Body body: BodyRegister
    ): Response<ResponseRegister>
}
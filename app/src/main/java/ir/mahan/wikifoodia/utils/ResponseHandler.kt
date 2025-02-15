package ir.mahan.wikifoodia.utils

import retrofit2.Response

open class ResponseHandler<T>(private val response: Response<T>) {

    fun generalNetworkResponse(): ResponseWrapper<T> {
        return when {
            response.message().contains("timeout") -> ResponseWrapper.Error("Timeout")
            response.code() == 401 -> ResponseWrapper.Error("You are not authorized")
            response.code() == 402 -> ResponseWrapper.Error("Your free plan finished")
            response.code() == 422 -> ResponseWrapper.Error("Api key not found!")
            response.code() == 500 -> ResponseWrapper.Error("Try again")
            response.isSuccessful -> ResponseWrapper.Success(response.body()!!)
            else -> ResponseWrapper.Error(response.message())
        }
    }
}
package ir.mahan.wikifoodia.utils

sealed class ResponseWrapper<T>(val data: T? = null, val message: String? = null) {

    class Loading<T>: ResponseWrapper<T>()
    class Success<T>(data: T): ResponseWrapper<T>(data)
    class Error<T>(message: String, data: T? = null): ResponseWrapper<T>(data, message)
}
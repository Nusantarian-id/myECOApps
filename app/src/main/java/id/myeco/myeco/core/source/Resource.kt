package id.myeco.myeco.core.source

sealed class Resource<T>(val data: T? = null, val message: String? = null, val code: Int? = null) {
    class Success<T>(message: String? = null, data: T? = null) : Resource<T>(data, message)
    class Error<T>(message: String, data: T? = null, code: Int? = null) : Resource<T>(data, message, code)
}
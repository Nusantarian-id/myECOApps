package id.myeco.myeco.core.source.remote.network

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(isNetworkActive: Observable<Any>) : Interceptor {
    private var isNetworkActive = false

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isNetworkActive) {
            throw NoConnectivityException()
        } else {
            chain.proceed(chain.request())
        }
    }

    init {
        isNetworkActive.subscribe(
            { _isNetworkActive -> this.isNetworkActive = _isNetworkActive as Boolean }
        ) { _error -> Log.e("NetworkActive error ", _error.message!!) }
    }
}

class NoConnectivityException : IOException() {
    override val message: String get() = "No network available, please check your WiFi or Data connection"
}
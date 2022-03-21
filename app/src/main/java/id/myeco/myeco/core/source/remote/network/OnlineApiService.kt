package id.myeco.myeco.core.source.remote.network

import id.myeco.myeco.core.source.remote.response.LoginResponse
import io.reactivex.rxjava3.core.Flowable
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OnlineApiService {
    //post data for login
    @Multipart
    @POST(Routing.LOGIN_URL)
    fun login(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Flowable<LoginResponse>
}
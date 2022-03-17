package id.myeco.myeco.core.source.remote.network

import id.myeco.myeco.core.source.remote.request.LoginModel
import id.myeco.myeco.core.source.remote.response.LoginResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OnlineApiService {
    //post data for login
    @Headers("content-Type: application/json; charset=UTF-8")
    @POST(Routing.LOGIN_URL)
    fun login(
        @Body data: LoginModel
    ): Flowable<LoginResponse>
}
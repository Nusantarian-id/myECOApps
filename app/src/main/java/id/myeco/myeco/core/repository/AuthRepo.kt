package id.myeco.myeco.core.repository

import android.util.Log
import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.local.SessionManager
import id.myeco.myeco.core.source.remote.network.OnlineApiService
import id.myeco.myeco.core.source.remote.response.LoginResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepo(private val apiService: OnlineApiService, private val sessionManager: SessionManager): AuthDataSource {
    override fun login(email: String, pass: String): Flowable<Resource<LoginResponse>> {
        val result = PublishSubject.create<Resource<LoginResponse>>()
        val emailLogin = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val passLogin = pass.toRequestBody("text/plain".toMediaTypeOrNull())

        apiService.login(emailLogin, passLogin).subscribeOn(Schedulers.io())
            .observeOn((AndroidSchedulers.mainThread())).take(1).subscribe({ response ->
                result.onNext(Resource.Success(data = response))
            }, {
                result.onNext(Resource.Error("Error Login"))
                Log.e("Error Login", it.message.toString())
            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun saveIdUser(idUser: String) = sessionManager.saveIdUser(idUser)

}
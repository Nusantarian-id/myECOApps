package id.myeco.myeco.core.repository

import android.util.Log
import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.local.SessionManager
import id.myeco.myeco.core.source.remote.network.EspApiService
import id.myeco.myeco.core.source.remote.network.OnlineApiService
import id.myeco.myeco.core.source.remote.request.LoginModel
import id.myeco.myeco.core.source.remote.response.LoginResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class AuthRepo(private val apiService: OnlineApiService, private val sessionManager: SessionManager): AuthDataSource {
    override fun login(email: String, pass: String): Flowable<Resource<LoginResponse>> {
        val user = LoginModel(email, pass)
        val result = PublishSubject.create<Resource<LoginResponse>>()

        apiService.login(user).subscribeOn(Schedulers.io())
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
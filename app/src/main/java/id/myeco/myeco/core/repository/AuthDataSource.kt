package id.myeco.myeco.core.repository

import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.remote.response.LoginResponse
import io.reactivex.rxjava3.core.Flowable

interface AuthDataSource {
    fun login(email: String, pass: String): Flowable<Resource<LoginResponse>>
    fun saveIdUser(idUser: String)
}
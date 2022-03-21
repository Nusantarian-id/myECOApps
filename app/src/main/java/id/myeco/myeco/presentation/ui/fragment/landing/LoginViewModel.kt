package id.myeco.myeco.presentation.ui.fragment.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import id.myeco.myeco.core.repository.AuthDataSource
import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.remote.response.LoginResponse

class LoginViewModel(private val authDataSource: AuthDataSource) : ViewModel() {
    fun login(email: String, pass: String): LiveData<Resource<LoginResponse>> =
        LiveDataReactiveStreams.fromPublisher(authDataSource.login(email, pass))

    fun saveIdUser(idUser: String) = authDataSource.saveIdUser(idUser)
}
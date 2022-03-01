package id.myeco.myeco.core.repository

import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.remote.response.PostDataResponse
import io.reactivex.rxjava3.core.Flowable

interface EspDataDataSource {
    fun sendData(ssid: String, pass: String, userId: String): Flowable<Resource<PostDataResponse>>
}
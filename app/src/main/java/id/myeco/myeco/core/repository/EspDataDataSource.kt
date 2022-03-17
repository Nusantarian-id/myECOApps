package id.myeco.myeco.core.repository

import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.remote.response.GetDataResponse
import io.reactivex.rxjava3.core.Flowable

interface EspDataDataSource {
    fun getData(ssid: String, pass: String, userId: String): Flowable<Resource<String>>
}
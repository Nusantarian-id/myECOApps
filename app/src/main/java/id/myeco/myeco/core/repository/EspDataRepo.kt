package id.myeco.myeco.core.repository

import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.remote.network.ApiService
import id.myeco.myeco.core.source.remote.request.PostDataModel
import id.myeco.myeco.core.source.remote.response.PostDataResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class EspDataRepo(private val apiService: ApiService): EspDataDataSource {
    override fun sendData(
        ssid: String,
        pass: String,
        userId: String
    ): Flowable<Resource<PostDataResponse>> {
        val result = PublishSubject.create<Resource<PostDataResponse>>()
        val data = PostDataModel(ssid, pass, userId)

        apiService.postData(data).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).take(1).subscribe({response ->
                result.onNext(Resource.Success(data = response))
            }, {
                result.onNext(Resource.Error(it.message.toString()))
            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
}
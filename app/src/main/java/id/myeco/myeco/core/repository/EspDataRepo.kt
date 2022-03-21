package id.myeco.myeco.core.repository

import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.remote.network.EspApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class EspDataRepo(private val apiService: EspApiService): EspDataDataSource {
    override fun getData(
        ssid: String,
        pass: String,
        userId: String
    ): Flowable<Resource<String>> {
        val result = PublishSubject.create<Resource<String>>()

        apiService.getData(ssid, pass, userId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).take(1).subscribe({
                result.onNext(Resource.Success("Success", it))
            }, {
                result.onNext(Resource.Error(it.message.toString()))
            })
        return result.toFlowable(BackpressureStrategy.BUFFER)
    }
}
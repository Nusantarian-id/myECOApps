package id.myeco.myeco.core.source.remote.network

import id.myeco.myeco.core.source.remote.request.PostDataModel
import id.myeco.myeco.core.source.remote.response.PostDataResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(Routing.POST_DATA_URL)
    fun postData(@Body data: PostDataModel): Flowable<PostDataResponse>
}
package id.myeco.myeco.core.source.remote.network

import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface EspApiService {
    //get data from esp
    @GET(Routing.GET_DATA_URL)
    fun getData(
        @Query("wifi") wifiName: String,
        @Query("passwifi") wifiPass: String,
        @Query("id_user") userId: String
    ): Flowable<String>

}
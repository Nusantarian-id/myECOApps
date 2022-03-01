package id.myeco.myeco.di

import id.myeco.myeco.core.source.remote.network.ApiService
import id.myeco.myeco.core.source.remote.network.Routing
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module{
    single{
        val retrofit = Retrofit.Builder().baseUrl(Routing.ESP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repoModule = module {  }

val viewModelModule = module { }
package id.myeco.myeco.di

import com.google.gson.GsonBuilder
import id.myeco.myeco.core.repository.AuthDataSource
import id.myeco.myeco.core.repository.AuthRepo
import id.myeco.myeco.core.repository.EspDataDataSource
import id.myeco.myeco.core.repository.EspDataRepo
import id.myeco.myeco.core.source.local.SessionManager
import id.myeco.myeco.core.source.remote.network.EspApiService
import id.myeco.myeco.core.source.remote.network.AuthInterceptor
import id.myeco.myeco.core.source.remote.network.OnlineApiService
import id.myeco.myeco.core.source.remote.network.Routing
import id.myeco.myeco.presentation.ui.fragment.landing.LoginViewModel
import id.myeco.myeco.presentation.ui.fragment.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val localModule = module {
    single {SessionManager(get())}
}

val networkModule = module{
    single {AuthInterceptor(get())}
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val gson = GsonBuilder().setLenient().create()
        val espRetrofit = Retrofit.Builder().baseUrl(Routing.ESP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(get())
            .build()

        val onlineRetrofit = Retrofit.Builder().baseUrl(Routing.WEB_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(get())
            .build()

        espRetrofit.create(EspApiService::class.java)
        onlineRetrofit.create(OnlineApiService::class.java)
    }
}

val repoModule = module {
    single<AuthDataSource>{AuthRepo(get(), get())}
    single<EspDataDataSource> {EspDataRepo(get())}
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}
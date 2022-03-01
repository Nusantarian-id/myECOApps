package id.myeco.myeco

import android.app.Application
import id.myeco.myeco.di.networkModule
import id.myeco.myeco.di.repoModule
import id.myeco.myeco.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                networkModule,
                repoModule,
                viewModelModule
            )
        }
    }
}
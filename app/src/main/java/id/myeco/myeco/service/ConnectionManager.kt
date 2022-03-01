package id.myeco.myeco.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.atomic.AtomicBoolean

class ConnectionManager(private val context: Context, private val callbackListener: ConnCallbackListener):
    LifecycleObserver {

    companion object {
        var hasNetwork = AtomicBoolean(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun registerYourReceiver() {
        context.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    internal fun unRegisterYourReceiver() {
        context.unregisterReceiver(connectionReceiver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
    }

    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val currentNetworkInfo = connectivityManager.activeNetworkInfo
            if (currentNetworkInfo != null && currentNetworkInfo.isConnected) {
                Log.d("connection receiver","Network Connected")
                hasNetwork.set(true)
                callbackListener.networkConnect()
            } else if (hasNetwork.get()) {
                Log.d("connection receiver","Network Disconnected")
                hasNetwork.set(false)
                callbackListener.networkDisconnect()
            }
        }
    }
}
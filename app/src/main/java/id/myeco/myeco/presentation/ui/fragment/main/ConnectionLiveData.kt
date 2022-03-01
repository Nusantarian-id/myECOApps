package id.myeco.myeco.presentation.ui.fragment.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import id.myeco.myeco.service.ConnCallbackListener
import id.myeco.myeco.service.ConnectionManager
import java.util.concurrent.atomic.AtomicBoolean

class ConnectionLiveData(private val context: Context) : MutableLiveData<Boolean>() {
    private var connCallbackListener: ConnCallbackListener? = null

    init {
        connCallbackListener = object : ConnCallbackListener {
            override fun networkConnect() {
                value = true
            }
            override fun networkDisconnect() {
                value = false
            }
        }
    }

    fun hasNetworkConnection(): Boolean {
        return hasNetwork.get()
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(connectionReceiver)
    }

    private val connectionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val currentNetworkInfo = connectivityManager.activeNetworkInfo
            if (currentNetworkInfo != null && currentNetworkInfo.isConnected) {
                Log.d("connection receiver","Network Connected")
                hasNetwork.set(true)
                value = true
            } else if (ConnectionManager.hasNetwork.get()) {
                Log.d("connection receiver","Network Disconnected")
                hasNetwork.set(false)
                value = false
            }
        }
    }

    companion object {
        var hasNetwork = AtomicBoolean(true)
    }
}
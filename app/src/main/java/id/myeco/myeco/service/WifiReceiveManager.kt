package id.myeco.myeco.service

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData

class WifiReceiveManager(private val app: Application, lifecycle: Lifecycle) : LifecycleObserver {

    private val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val wifiResponse = MutableLiveData<WifiResponse>()

    companion object {
        var disconnecting: Boolean = false
        var networkSSID: String = ""
        var networkId: Int = -1
    }

    fun wifiResponse(): MutableLiveData<WifiResponse> = wifiResponse

    // create intent filter for wifi connection receiver
    private val intentConnectionReceiver: IntentFilter
        get() {
            val randomIntentFilter = IntentFilter()
            randomIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            randomIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            randomIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            return randomIntentFilter
        }

    private val wifiConnectionReceiver = object: BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            when (intent.action) {
                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    val wifiInfo = wifiManager.connectionInfo
                    var networkName = wifiInfo.ssid
                    networkName = networkName.replace("\"", "")
                    if (networkSSID == networkName && !disconnecting)
                        wifiResponse.value = WifiResponse.connected()
                    else if (networkSSID != networkName && disconnecting)
                        wifiResponse.value = WifiResponse.disconnected()
                }
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                    val sb = StringBuilder()
                    val wifiList = wifiManager.scanResults
                    val deviceList = arrayListOf<String>()
                    for (scanResult in wifiList) {
                        sb.append("\n").append(scanResult.SSID).append(" - ")
                            .append(scanResult.capabilities)
                        deviceList.add(scanResult.SSID.toString() + " - " + scanResult.capabilities)
                    }
                }
            }
        }
    }

    private val lifecycleObserver = LifecycleEventObserver { source, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                app.registerReceiver(
                    wifiConnectionReceiver,
                    intentConnectionReceiver
                )
            }
            Lifecycle.Event.ON_STOP -> {
                try {
                    app.unregisterReceiver(wifiConnectionReceiver)
                } catch (e: IllegalArgumentException) {
                    Log.e("unregist receiver", e.message.toString())
                }
            }
        }
    }

    fun connectWifi(ssid: String, pass: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                Log.d("Connect Pre-Q", "Pre Q Connection")
                val wifiConfig = WifiConfiguration()
                wifiConfig.SSID = "\"" + ssid + "\""
                wifiConfig.preSharedKey = "\"" + pass + "\""
                val netId = wifiManager.addNetwork(wifiConfig)
                wifiManager.disconnect()
                wifiManager.enableNetwork(netId, true)
                wifiManager.reconnect()
            } catch (e: Exception) {
                Log.e("Connect Pre-Q", e.toString())
            }
        } else {
            Log.e("Connect Q", "Q Connection")
            val specifier = WifiNetworkSpecifier.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(pass)
                .build()

            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .setNetworkSpecifier(specifier)
                .build()

            val manager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    if (ssid.contains("Test-V4-2"))
                        manager.bindProcessToNetwork(network)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    // This is to stop the looping request for OnePlus & Xiaomi models
                    manager.bindProcessToNetwork(null)
                }
            }
            manager.requestNetwork(networkRequest, networkCallback)
        }
    }

    fun disconnectFromWifi() {
        Log.d("disconnect from wifi", "WiFi disconnectFromWifi")
        disconnecting = true
        wifiResponse.value = WifiResponse.disconnecting()
        wifiManager.disconnect()
        wifiManager.disableNetwork(networkId)
        wifiManager.removeNetwork(networkId)
        wifiManager.reconnect() // reconnect to previous network
    }

    init {
        lifecycle.addObserver(lifecycleObserver)
    }
}
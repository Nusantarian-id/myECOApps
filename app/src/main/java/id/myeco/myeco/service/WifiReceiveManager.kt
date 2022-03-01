package id.myeco.myeco.service

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkCapabilities
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent

class WifiReceiveManager(private val app: Application, lifecycle: Lifecycle) : LifecycleObserver {

    private val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val wifiResponse = MutableLiveData<WifiResponse>()

    companion object {
        var disconnecting: Boolean = false
        var networkSSID: String = ""
        var networkPassword: String = ""
        var previousNetworkSSID: String = ""
        var networkId: Int = -1
    }

    fun wifiResponse(): MutableLiveData<WifiResponse> = wifiResponse

    // create intent filter for wifi connection receiver
    private val intentConnectionReceiver: IntentFilter
        get() {
            val randomIntentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
            randomIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            return randomIntentFilter
        }

    private val wifiConnectionReceiver = object: BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            val action = intent.action
            if (!TextUtils.isEmpty(action)){
                when(action){
                    WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                        val wifiInfo = wifiManager.connectionInfo
                        var networkName = wifiInfo.ssid
                        networkName =networkName.replace("\"", "")
                        if (networkSSID == networkName && !disconnecting)
                            wifiResponse.value = WifiResponse(WifiStatus.CONNECTED, null, null)
                        else if (networkSSID != networkName && disconnecting)
                            wifiResponse.value = WifiResponse(WifiStatus.DISCONNECTED, null, null)
                    }
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun registerYourReceiver() {
       app.registerReceiver(wifiConnectionReceiver, intentConnectionReceiver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    internal fun unRegisterYourReceiver() {
        try {
            app.unregisterReceiver(wifiConnectionReceiver)
        } catch (e : IllegalArgumentException) {
           Log.e("unregist receiver",e.message.toString())
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNetworkId(networkSSID: String): Int {
        val list = wifiManager.configuredNetworks
        for (i in list) {
            if (i.SSID != null && i.SSID == "\"" + networkSSID + "\"") {
                return i.networkId
            }
        }
        return -1
    }

    fun connectWifi(ssid: String, password: String) {

        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            Log.d("onReceive","onReceive: cannot use connection without passing in a proper wifi SSID and password.")
            return
        }

        networkSSID = ssid
        networkPassword = password

        wifiResponse.value = WifiResponse(WifiStatus.CONNECTING, null, null)
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }

        val wifiInfo = wifiManager.connectionInfo
        if(wifiInfo.ssid == networkSSID) {
            wifiResponse.value = WifiResponse(WifiStatus.CONNECTED, null, null)
            return
        }
        previousNetworkSSID = wifiInfo.ssid
        disconnecting = false
        connectToWifi()
    }

    private fun connectToWifi() {
        Log.d("connect to wifi","WiFi connectToWifi")
        val conf = WifiConfiguration()
        conf.SSID = String.format("\"%s\"", networkSSID);
        conf.preSharedKey = String.format("\"%s\"", networkPassword);
        conf.status = WifiConfiguration.Status.ENABLED;
        conf.hiddenSSID = true;

        networkId = wifiManager.addNetwork(conf)
        if(networkId == -1) {
            networkId = getNetworkId(networkSSID)
        }

        wifiManager.disconnect()
        wifiManager.enableNetwork(networkId, true)
        wifiManager.reconnect()
    }

    fun disconnectFromWifi() {
        Log.d("disconnect from wifi","WiFi disconnectFromWifi")
        disconnecting = true
        wifiResponse.value = WifiResponse(WifiStatus.DISCONNECTING, null, null)
        wifiManager.disconnect()
        wifiManager.disableNetwork(networkId)
        wifiManager.removeNetwork(networkId)
        wifiManager.reconnect() // reconnect to previous network
    }

    init {
        lifecycle.addObserver(this)
    }
}
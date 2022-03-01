package id.myeco.myeco.service

data class WifiResponse(
    val status: WifiStatus,
    val data: String?,
    val error: Throwable?
) {
    fun connecting(): WifiResponse = WifiResponse(WifiStatus.CONNECTING, null, null)
    fun connected(): WifiResponse = WifiResponse(WifiStatus.CONNECTED, null, null)
    fun disconnected(): WifiResponse = WifiResponse(WifiStatus.DISCONNECTED, null, null)
    fun disconnecting(): WifiResponse = WifiResponse(WifiStatus.DISCONNECTING, null, null)
    fun error(): WifiResponse = WifiResponse(WifiStatus.ERROR, null, null)
}

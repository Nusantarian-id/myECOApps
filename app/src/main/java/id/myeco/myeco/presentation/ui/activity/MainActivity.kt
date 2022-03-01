package id.myeco.myeco.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import id.myeco.myeco.databinding.ActivityMainBinding
import id.myeco.myeco.presentation.ui.fragment.main.ConnectionLiveData
import id.myeco.myeco.presentation.ui.fragment.main.MainFragment
import id.myeco.myeco.service.WifiReceiveManager
import id.myeco.myeco.service.WifiStatus

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var conn: ConnectionLiveData
    private lateinit var manager: WifiReceiveManager
    private var waitConn: Boolean = false
    private lateinit var wifiStats: WifiStatus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        conn = ConnectionLiveData(this)
        conn.observe(this) { connected ->
            if (connected && waitConn){
                waitConn = false
                if (wifiStats == WifiStatus.CONNECTED){

                }
            }
        }
    }
}
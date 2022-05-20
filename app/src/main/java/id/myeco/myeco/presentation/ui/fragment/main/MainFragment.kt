package id.myeco.myeco.presentation.ui.fragment.main

import android.content.Context
import android.content.Intent
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.myeco.myeco.databinding.FragmentMainBinding
import id.myeco.myeco.presentation.ui.activity.WebviewActivity
import id.myeco.myeco.utils.removeQuotation
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tilWifi.editText?.addTextChangedListener(watcher)

        binding.btnConnect.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        binding.btnSubmit.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            val pref = requireContext().applicationContext.getSharedPreferences("myEco", 0)
            val ssid = pref.getString("ssid", "")
            val pass = pref.getString("pass", "")
            val userId = pref.getString("data", "")
            sendData(ssid!!, pass!!, userId!!)
        }
        checkWifiConnection()
    }

    override fun onResume() {
        super.onResume()
        checkWifiConnection()
    }

    private fun checkWifiConnection() {
        val wifiManager =
            requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
            val ssid = wifiInfo.ssid
            binding.tilWifi.editText?.setText(ssid.removeQuotation())
        }
    }

    private fun sendData(ssid: String, pass: String, userId: String) {
        viewModel.getData(ssid, pass, userId).observe(viewLifecycleOwner) {
            binding.progress.visibility = View.GONE
            startActivity(Intent(requireContext(), WebviewActivity::class.java))
        }
    }

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            val wifi = binding.tilWifi.editText!!.text.toString()
            binding.btnSubmit.isEnabled = wifi.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
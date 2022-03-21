package id.myeco.myeco.presentation.ui.fragment.main

import android.content.Context
import android.content.Intent
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import id.myeco.myeco.R
import id.myeco.myeco.databinding.FragmentConnectionBinding
import id.myeco.myeco.utils.removeQuotation
import id.myeco.myeco.utils.toastShort

class ConnectionFragment : Fragment() {

    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tilWifi.editText!!.addTextChangedListener(watcher)
        binding.tilPass.editText!!.addTextChangedListener(watcher)

        binding.btnConnect.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        binding.btnNext.setOnClickListener {
            // save data
            val pref = requireContext().applicationContext.getSharedPreferences("MyEco", 0)
            val editor = pref.edit()
            editor.putString("ssid", binding.tilWifi.editText?.text.toString().removeQuotation())
            editor.putString("pass", binding.tilPass.editText?.text.toString().removeQuotation())
            editor.apply()

            findNavController()
                .navigate(ConnectionFragmentDirections.actionConnectionFragmentToMainFragment())
        }

        checkWifiConnection()
    }

    override fun onResume() {
        super.onResume()
        checkWifiConnection()
    }

    private fun checkWifiConnection(){
        val wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        if (wifiInfo.supplicantState == SupplicantState.COMPLETED){
            val ssid = wifiInfo.ssid
            binding.tilWifi.editText?.setText(ssid.removeQuotation())
        }
    }

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            val wifi = binding.tilWifi.editText!!.text.toString()
            val pass = binding.tilPass.editText!!.text.toString()
            binding.btnNext.isEnabled = wifi.isNotEmpty() && pass.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
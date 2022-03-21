package id.myeco.myeco.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.myeco.myeco.databinding.DialogConnectWifiBinding

class ConnectWifiDialog : DialogFragment() {

    private var _binding: DialogConnectWifiBinding? = null
    private val binding get() = _binding!!

    var onSendDataButtonClick: (() -> Unit)? = null
    var onCancelButtonClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogConnectWifiBinding.inflate(layoutInflater)

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setTitle("Input Pass & UserId")

        binding.btnSend.setOnClickListener {
            onSendDataButtonClick?.invoke()
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            onCancelButtonClick?.invoke()
            dialog?.dismiss()
        }

        dialogBuilder.setView(binding.root)

        return dialogBuilder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
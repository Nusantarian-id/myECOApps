package id.myeco.myeco.presentation.ui.fragment.landing

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.databinding.FragmentLoginBinding
import id.myeco.myeco.presentation.ui.activity.MainActivity
import id.myeco.myeco.utils.toastShort
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            loginAccount()
        }
    }

    //login account with firebase
    private fun loginAccount() {
        val email = binding.tilEmail.editText?.text.toString().trim()
        val pass = binding.tilPass.editText?.text.toString().trim()
        val empty = "Input ini Kosong"
        val invalid = "Alamat Email Tidak Valid"

        // check email field
        if (email.isBlank()) {
            binding.tilEmail.error = empty
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = invalid
        } else {
            binding.tilEmail.error = null
        }

        // check pass field
        if (pass.isBlank()) {
            binding.tvTitleLogin2.error = empty
        } else {
            binding.tilPass.error = null
        }

        if (binding.tilEmail.error == null && binding.tilPass.error == null) {
            binding.progress.visibility = View.VISIBLE
            viewModel.login(email, pass).observe(viewLifecycleOwner) { response ->
                binding.tilEmail.error = null
                binding.tilPass.error = null
                binding.tilEmail.isErrorEnabled
                binding.tilPass.isErrorEnabled

                binding.progress.visibility = View.GONE
                when (response) {
                    is Resource.Success -> {
                        viewModel.saveIdUser(response.data?.id!!.toString())
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.putExtra("idUser", response.data.id.toString())
                        startActivity(intent)
                        requireActivity().finishAffinity()
                    }
                    is Resource.Error -> context?.toastShort(response.message.toString())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
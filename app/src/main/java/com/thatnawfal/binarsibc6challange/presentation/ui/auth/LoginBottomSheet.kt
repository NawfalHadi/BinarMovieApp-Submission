package com.thatnawfal.binarsibc6challange.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thatnawfal.binarsibc6challange.databinding.FragmentLoginBottomSheetBinding
import com.thatnawfal.binarsibc6challange.presentation.logic.account.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginBottomSheet(
    private val listener : OnLoginListener
) : BottomSheetDialogFragment() {

    private var _binding : FragmentLoginBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAccountData().observe(requireActivity()) {
            Log.e("Account Data ", " Email : ${it.email} Username : ${it.username} Pass: ${it.password}")
        }

        binding.btnLoginRegister.setOnClickListener {
            listener.toRegister()
        }

        binding.btnLoginLogin.setOnClickListener {
            if (formValidation()){
                listener.loginSuccess()
                dismiss()
            }
        }


    }

    private fun formValidation(): Boolean {
        val email = binding.etLoginEmail.text.toString()
        val pass = binding.etLoginPassword.text.toString()
        var validateForm = true

        if (email.isEmpty()) {
            validateForm = false
            binding.tilEtLoginEmail.isErrorEnabled = true
            binding.tilEtLoginEmail.error = "Don't Empty The Field"
        } else { binding.tilEtLoginEmail.isErrorEnabled = false }

        if (pass.isEmpty()) {
            validateForm = false
            binding.tilEtLoginPassword.isErrorEnabled = true
            binding.tilEtLoginPassword.error = "Don't Empty The Field"
        } else {
            validateForm = false
            if (verifyPassword(email, pass)) {
                validateForm = true
                binding.tilEtLoginPassword.isErrorEnabled = false
            } else {
                binding.tilEtLoginPassword.isErrorEnabled = true
                    binding.tilEtLoginPassword.error = "Check Your Email & Password!"
            }
        }
        return validateForm
    }

    private fun verifyPassword(email: String, pass: String): Boolean {
        var validateForm = false
        viewModel.getAccountData().observe(requireActivity()) {
            validateForm = email == it.email && pass == it.password
        }
        return validateForm
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface OnLoginListener {
    fun toRegister()
    fun loginSuccess()
}

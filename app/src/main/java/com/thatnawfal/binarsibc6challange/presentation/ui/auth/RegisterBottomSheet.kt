package com.thatnawfal.binarsibc6challange.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thatnawfal.binarsibc6challange.databinding.FragmentRegisterBottomSheetBinding
import com.thatnawfal.binarsibc6challange.presentation.logic.account.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterBottomSheet(
    private val listener : OnRegisterListener
) : BottomSheetDialogFragment() {

    private var _binding : FragmentRegisterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            dismiss()
            listener.toLogin()
        }
        binding.btnRegister.setOnClickListener {
            if (formValidation()) {
                submitRegisterForm()
                dismiss()
                listener.toLogin()
            }
        }
    }

    private fun submitRegisterForm() {
        viewModel.registerAccount(
            username = binding.etRegUsername.text.toString(),
            password = binding.etRegPass.text.toString(),
            email = binding.etRegEmail.text.toString()
        )
    }

    private fun formValidation() : Boolean{
        val username = binding.etRegUsername.text.toString()
        val email = binding.etRegEmail.text.toString()
        val password = binding.etRegPass.text.toString()
        val comfPass = binding.etRegComfPass.text.toString()
        var validateForm = true

        if (username.isEmpty()){
            validateForm = false
            binding.tilEtRegUsername.isErrorEnabled = true
            binding.tilEtRegUsername.error = "Username Can't Be Empty"
        } else { binding.tilEtRegUsername.isErrorEnabled = false }

        if (email.isEmpty()){
            validateForm = false
            binding.tilEtRegEmail.isErrorEnabled = true
            binding.tilEtRegEmail.error = "Email Can't Be Empty"
        } else { binding.tilEtRegEmail.isErrorEnabled = false }

        if (password.isEmpty()){
            validateForm = false
            binding.tilEtRegPass.isErrorEnabled = true
            binding.tilEtRegPass.error = "Password Can't Be Empty"
        } else { binding.tilEtRegPass.isErrorEnabled = false }

        if (comfPass.isEmpty()){
            validateForm = false
            binding.tilEtRegComfPass.isErrorEnabled = true
            binding.tilEtRegComfPass.error = "Confirmation Password Can't Be Empty"
        } else {
            if (comfPass != password) {
                validateForm = false
                binding.tilEtRegComfPass.isErrorEnabled = true
                binding.tilEtRegComfPass.error = "Password Didn't Match"
            } else { binding.tilEtRegComfPass.isErrorEnabled = false }
        }

        return validateForm
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

interface OnRegisterListener{
    fun toLogin()
}
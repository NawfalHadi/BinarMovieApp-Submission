package com.thatnawfal.binarsibc6challange.presentation.ui.home.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.thatnawfal.binarsibc6challange.data.local.model.AccountModel
import com.thatnawfal.binarsibc6challange.databinding.FragmentProfileBinding
import com.thatnawfal.binarsibc6challange.presentation.logic.account.AccountViewModel
import com.thatnawfal.binarsibc6challange.presentation.ui.SplashScreenActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AccountViewModel by viewModels()
    private lateinit var account: AccountModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAccountData().observe(requireActivity()){
            bindingView(it)
            account = it
        }

        binding.btnConfirm.setOnClickListener {
            editAccountData()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.setLoginStatus(false)
            startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun editAccountData() {
        val username = binding.etRegUsername.text.toString()
        val email = binding.etRegEmail.text.toString()
        val password = binding.etRegNewPass.text.toString()

        if (changedVerify(username, email, password)){
            if (username.isEmpty()){
                binding.tilEtRegUsername.isErrorEnabled = true
                binding.tilEtRegUsername.error = "Username Can't Be Empty"
            } else {
                binding.tilEtRegUsername.isErrorEnabled = false
                viewModel.setUsername(username)
            }

            if (email.isEmpty()){
                binding.tilEtRegEmail.isErrorEnabled = true
                binding.tilEtRegEmail.error = "Email Can't Be Empty"
            } else {
                binding.tilEtRegEmail.isErrorEnabled = false
                viewModel.setEmail(email)
            }

            if (password.isNotEmpty()){
                if (password == account.password) {
                    binding.tilEtRegNewPass.isErrorEnabled = true
                    binding.tilEtRegNewPass.error = "Don't Use The Same Password"
                } else {
                    binding.tilEtRegNewPass.isErrorEnabled = false
                    viewModel.setPassword(password)
                }
            }
            findNavController().popBackStack()
        }

    }

    private fun changedVerify(username: String, email: String, password: String): Boolean {
        var validateStatus = false

        if (username != account.username
            || email != account.email
            || (password != account.password && password.isNotEmpty())) {

            validateStatus = binding.etRegPass.text.toString() == account.password
            if (!validateStatus) {
                binding.tilEtRegPass.isErrorEnabled = true
                binding.tilEtRegPass.error = "Check Your Password"
            }
        } else {
            binding.tilEtRegPass.isErrorEnabled = false
        }

        return validateStatus
    }


    private fun bindingView(account: AccountModel) {
        binding.textView.text = "Hi, ${account.username}"
        binding.etRegUsername.setText(account.username)
        binding.etRegEmail.setText(account.email)
    }
}
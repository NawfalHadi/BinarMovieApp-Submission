package com.thatnawfal.binarsibc6challange.presentation.ui.home.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thatnawfal.binarsibc6challange.data.local.model.AccountModel
import com.thatnawfal.binarsibc6challange.databinding.FragmentProfileBinding
import com.thatnawfal.binarsibc6challange.presentation.logic.account.AccountViewModel
import com.thatnawfal.binarsibc6challange.presentation.ui.SplashScreenActivity
import com.thatnawfal.binarsibc6challange.presentation.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        const val REQUEST_CODE_PERMISSION = 100
    }

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : AccountViewModel by viewModels()
    private lateinit var account: AccountModel

    private lateinit var uri: Uri
    private val cameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            result -> if (result) { saveToDataStore(uri) }
    }

    private val galleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        result?.let { saveToDataStore(it) }
    }

    private fun saveToDataStore(it: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(it)
        val bitmapPhoto = BitmapFactory.decodeStream(inputStream)
        binding.btnImage.setImageBitmap(bitmapPhoto)
        bitmapToString(bitmapPhoto)?.let { stringImage -> viewModel.setImage(stringImage) }
    }

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

        binding.btnImage.setOnClickListener {
            checkingPermission()
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

    private fun bindingView(it: AccountModel) {
        if (it.image != ""){
            binding.btnImage.setImageBitmap(stringToBitmap(it.image))
        }

        binding.textView.text = "Hi, ${it.username}"
        binding.etRegUsername.setText(it.username)
        binding.etRegEmail.setText(it.email)
    }

    private fun bitmapToString(bitmapPhoto: Bitmap?): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmapPhoto?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun stringToBitmap(str: String): Bitmap? {
        return try {
            val encodeString = Base64.decode(str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeString, 0 ,encodeString.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

    private fun checkingPermission() {
        if (isGranted(
                requireActivity() as HomeActivity, Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") {_,_ -> openGallery()}
            .setNegativeButton("Camera") {_, _ -> openCamera()}
            .show()
    }

    private fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )
        cameraResult.launch(uri)
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun isGranted(
        activity: HomeActivity,
        permission: String,
        permissions: Array<String>,
        requestCodePermission: Int
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCodePermission)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permssion is denied, Lorem Ipsum .....")
            .setPositiveButton(
                "App Setting"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS

                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") {dialog, _ -> dialog.cancel()}
            .show()
    }
}

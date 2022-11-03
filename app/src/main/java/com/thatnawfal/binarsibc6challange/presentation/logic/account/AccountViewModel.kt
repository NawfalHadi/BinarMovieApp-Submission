package com.thatnawfal.binarsibc6challange.presentation.logic.account

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.work.*
import com.thatnawfal.binarsibc6challange.data.local.model.AccountModel
import com.thatnawfal.binarsibc6challange.data.repository.LocalRepository
import com.thatnawfal.binarsibc6challange.utils.IMAGE_MANIPULATION_WORK_NAME
import com.thatnawfal.binarsibc6challange.utils.KEY_IMAGE_URI
import com.thatnawfal.binarsibc6challange.utils.TAG_OUTPUT
import com.thatnawfal.binarsibc6challange.worker.BlurWorker
import com.thatnawfal.binarsibc6challange.worker.ClearnUpWorker
import com.thatnawfal.binarsibc6challange.worker.SaveImageToFileWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: LocalRepository,
    application : Application
)  : ViewModel()  {

    // Blurring Var - Start

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    private val workRequest = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>>

    // Blurring Var - End

    init {
        imageUri = getImageUri(application.applicationContext)
        outputWorkInfos = workRequest.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    fun registerAccount(username: String, email : String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.registerAccount(
                username,
                password,
                email
            )
        }
    }

    fun getAccountData(): LiveData<AccountModel> {
        return repository.getAccountData().asLiveData()
    }

    fun getLoginStatus(): LiveData<Boolean> {
        return repository.getLoginStatus().asLiveData()
    }

    // Set Data

    fun setLoginStatus(status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setLoginStatus(status)
        }
    }

    fun setUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setUsername(username)
        }
    }

    fun setEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setEmail(email)
        }
    }

    fun setPassword(password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setPassword(password)
        }
    }

    fun setImage(image: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setImage(image)
        }
    }

    // Blurring Function - Start

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
//            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
//            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Log.e(ContentValues.TAG, "uriOrNull: $uriString")
            Uri.parse(uriString)
        } else {
            null
        }
    }

    internal fun applyBlur(blurLevel: Int) {

        var continuation = workRequest
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(ClearnUpWorker::class.java)
            )

        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        val constraint = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val save = OneTimeWorkRequest.Builder(
            SaveImageToFileWorker::class.java
        ).setConstraints(constraint).addTag(TAG_OUTPUT).build()

        continuation = continuation.then(save)
        continuation.enqueue()

    }

    internal fun setOutputUri(outputImageUri: String?) {
        Log.e(ContentValues.TAG, "uriOrNull: $outputImageUri")
        outputUri = uriOrNull(outputImageUri)
        Log.e(ContentValues.TAG, "uriOrNull: $outputUri")
    }

    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }

        return builder.build()
    }

//    internal fun cancelWork(){
//        workRequest.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
//    }

    // Blurring Function - End

}
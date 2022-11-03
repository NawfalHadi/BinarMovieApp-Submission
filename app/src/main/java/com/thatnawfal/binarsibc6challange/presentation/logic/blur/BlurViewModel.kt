package com.thatnawfal.binarsibc6challange.presentation.logic.blur

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import androidx.work.WorkManager
import com.thatnawfal.binarsibc6challange.utils.IMAGE_MANIPULATION_WORK_NAME
import com.thatnawfal.binarsibc6challange.utils.KEY_IMAGE_URI
import com.thatnawfal.binarsibc6challange.utils.TAG_OUTPUT
import com.thatnawfal.binarsibc6challange.worker.BlurWorker
import com.thatnawfal.binarsibc6challange.worker.ClearnUpWorker
import com.thatnawfal.binarsibc6challange.worker.SaveImageToFileWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BlurViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    private val workRequest = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>>

    init {
        imageUri = getImageUri(application.applicationContext)
        outputWorkInfos = workRequest.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

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

    internal fun cancelWork(){
        workRequest.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

//    class BlurViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
//
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return if (modelClass.isAssignableFrom(BlurViewModel::class.java)) {
//                BlurViewModel(application) as T
//            } else {
//                throw IllegalArgumentException("Unknown ViewModel class")
//            }
//        }
//    }
}


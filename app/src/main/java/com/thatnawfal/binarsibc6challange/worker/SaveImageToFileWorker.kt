package com.thatnawfal.binarsibc6challange.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.thatnawfal.binarsibc6challange.utils.KEY_IMAGE_URI
import com.thatnawfal.binarsibc6challange.utils.makeStatusNotification
import com.thatnawfal.binarsibc6challange.utils.sleep
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "SaveImageToFileWorker : "
class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    private val title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override fun doWork(): Result {
        makeStatusNotification("Saving Image", applicationContext)
        sleep()

        val resolver = applicationContext.contentResolver
        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

            val imageUri = MediaStore.Images.Media.insertImage(
                resolver, bitmap, title, dateFormatter.format(Date()))

            if(!imageUri.isNullOrEmpty()) {
                val output = workDataOf(KEY_IMAGE_URI to imageUri)
                Result.success(output)
            } else {
                Result.failure()
            }


        } catch (e : Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
package com.thatnawfal.binarsibc6challange.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.thatnawfal.binarsibc6challange.utils.OUTPUT_PATH
import com.thatnawfal.binarsibc6challange.utils.makeStatusNotification
import com.thatnawfal.binarsibc6challange.utils.sleep
import java.io.File

private const val TAG = "ClearnUpWorker"
class ClearnUpWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        makeStatusNotification("Cleaning Up Temp FIle", applicationContext)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null ) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")){
                            val deleted = entry.delete()
                            Log.i(TAG, "delete $name - $deleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (e : Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
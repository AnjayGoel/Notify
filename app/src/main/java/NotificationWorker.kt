package com.anjay.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*
import java.util.concurrent.TimeUnit

class NotificationWorker(var con: Context, workerParams: WorkerParameters) :
    Worker(con, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.
        lg("-------------working--------------")

        /*var t = FacebookHandler.getLatestUpdateTime()

        if (t!=lastUpdated){
            lastUpdated = t
            lg("-----------------------------------Updated-----------------------")
        }
        */

        startNewRequest(con)
        return Result.success()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = con.getString(R.string.notification_channel)
            val descriptionText = con.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("SilverBug", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                con.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        fun startNewRequest(con: Context) {
            var builder = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            builder.setInitialDelay(2, TimeUnit.SECONDS)
            builder.setConstraints(Constraints.NONE)
            var workRequest = builder.build()
            WorkManager.getInstance(con).enqueue(workRequest)
        }
    }

}
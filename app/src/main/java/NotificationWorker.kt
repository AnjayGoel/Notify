package com.anjay.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

class NotificationWorker(var con: Context, workerParams: WorkerParameters) :
    Worker(con, workerParams) {

    override fun doWork(): Result {
        lg("Working")
        if (DataHandler.getInstance(con).cardDao.getLastUpdate() != FacebookHandler.getLatestUpdateTime()) {
            lg("Updated")
            var posts = FacebookHandler.latestPosts()
            lg(posts.toString())
            if (posts != null) {
                DataHandler.getInstance(con).addCards(posts)
            }
            notify("New Posts", "You have some unread posts", con)
        }

        startNewRequest(con)
        return Result.success()
    }


    companion object {

        fun createNotificationChannel(
            context: Context,
            importance: Int,
            showBadge: Boolean,
            name: String,
            description: String
        ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        }

        fun notify(title: String, content: String, context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
            val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
                setSmallIcon(R.drawable.dummy)
                setContentTitle(title)
                setChannelId(channelId)
                setContentText(content)
                priority = NotificationCompat.PRIORITY_HIGH
                setAutoCancel(true)
                setContentIntent(pendingIntent)
            }
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(2311, notificationBuilder.build())


        }
        fun startNewRequest(con: Context) {
            var builder = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            builder.setInitialDelay(5, TimeUnit.SECONDS)
            builder.setConstraints(Constraints.NONE)
            var workRequest = builder.build()
            WorkManager.getInstance(con).cancelAllWork()
            WorkManager.getInstance(con).enqueue(workRequest)
        }
    }

}
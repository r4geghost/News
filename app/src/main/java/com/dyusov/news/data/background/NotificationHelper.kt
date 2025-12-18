package com.dyusov.news.data.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dyusov.news.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager?
) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // check version of API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, // channel id
                context.getString(R.string.new_articles), // label for user
                NotificationManager.IMPORTANCE_DEFAULT // notification with sound + icon tray
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showNewArticlesNotification(topics: List<String>) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_breaking_news)
            .setContentTitle(context.getString(R.string.new_articles_notification_title))
            .setContentText(
                context.getString(
                    R.string.updated_subscriptions,
                    topics.size,
                    topics.joinToString(", ")
                ))
            .build()
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val CHANNEL_ID = "new_articles"
        private const val NOTIFICATION_ID = 1
    }
}
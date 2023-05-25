package com.jasonstudio.cookbook2.Network

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.ext.MyCustomTarget
import com.jasonstudio.cookbook2.util.LogUtil
import com.jasonstudio.cookbook2.view.activity.RecipeActivity


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                packageName, "Cook Book", NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)!!
            manager.createNotificationChannel(channel)
        }
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        LogUtil.log("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            LogUtil.log("Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            LogUtil.log("Message Notification clickAction: ${it.clickAction}")
            LogUtil.log("Message Notification Body: ${it.body}")
            it.clickAction?.let { actString ->
                when (ClickAction.valueOf(actString)) {
                    ClickAction.RECIPE -> {
                        val notificationIntent = Intent(this@MyFirebaseMessagingService, RecipeActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        notificationIntent.putExtra("id", remoteMessage.data["id"])
                        notificationIntent.putExtra("title", remoteMessage.data["title"])
                        val contentIntent = PendingIntent.getActivity(
                            this@MyFirebaseMessagingService,
                            0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                        Glide.with(this@MyFirebaseMessagingService)
                            .asBitmap()
                            .load("https://spoonacular.com/cdn/ingredients_100x100/sugar-in-bowl.png")
                            .timeout(60000)
                            .into(object : MyCustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    val noti: Notification = NotificationCompat.Builder(this@MyFirebaseMessagingService, this@MyFirebaseMessagingService.packageName)
                                        .setContentTitle(it.title)
                                        .setContentText(it.body)
                                        .setSmallIcon(R.drawable.cook_book_icon)
                                        .setLargeIcon(resource)
                                        .setContentIntent(contentIntent)
                                        .build()
                                    NotificationManagerCompat.from(this@MyFirebaseMessagingService).apply {
                                        if (ActivityCompat.checkSelfPermission(
                                                this@MyFirebaseMessagingService,
                                                Manifest.permission.POST_NOTIFICATIONS
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            return
                                        }
                                        notify(1, noti)
                                    }
                                }
                            })
                    }
                }
            }
        }
    }
}

enum class ClickAction(val rawValue: String) {
    RECIPE("RECIPE"),
}
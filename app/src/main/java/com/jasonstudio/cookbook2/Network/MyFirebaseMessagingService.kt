package com.jasonstudio.cookbook2.Network

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jasonstudio.cookbook2.R
import com.jasonstudio.cookbook2.ext.MyCustomTarget
import com.jasonstudio.cookbook2.util.LogUtil
import com.jasonstudio.cookbook2.view.activity.AboutUsActivity
import com.jasonstudio.cookbook2.view.activity.BiometricActivity
import com.jasonstudio.cookbook2.view.activity.FavouriteRecipeActivity
import com.jasonstudio.cookbook2.view.activity.MainActivity
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
                var notificationIntent: Intent
                when (ClickAction.valueOf(actString)) {
                    ClickAction.RECIPE -> {
                        notificationIntent = Intent(this@MyFirebaseMessagingService, RecipeActivity::class.java)
                        notificationIntent.putExtra("id", remoteMessage.data["id"])
                        notificationIntent.putExtra("title", remoteMessage.data["title"])
                    }

                    ClickAction.HOME -> {
                        notificationIntent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
                    }
                    ClickAction.BIOMETRIC -> {
                        notificationIntent = Intent(this@MyFirebaseMessagingService, BiometricActivity::class.java)
                    }
                    ClickAction.ABOUT_US -> {
                        notificationIntent = Intent(this@MyFirebaseMessagingService, AboutUsActivity::class.java)
                    }
                    ClickAction.FAVOURITE_RECIPE -> {
                        notificationIntent = Intent(this@MyFirebaseMessagingService, FavouriteRecipeActivity::class.java)
                    }
                }

                val stackBuilder = TaskStackBuilder.create(this)

                stackBuilder.addNextIntentWithParentStack(notificationIntent)
                val contentIntent = stackBuilder.getPendingIntent(
                    0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                Glide.with(this@MyFirebaseMessagingService)
                    .asBitmap()
                    .load("https://spoonacular.com/recipeImages/${remoteMessage.data["id"]}-556x370.jpg")
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

enum class ClickAction {
    RECIPE,
    HOME,
    BIOMETRIC,
    ABOUT_US,
    FAVOURITE_RECIPE,
}
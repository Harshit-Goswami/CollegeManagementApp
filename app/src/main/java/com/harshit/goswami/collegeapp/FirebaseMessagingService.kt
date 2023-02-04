package com.harshit.goswami.collegeapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.harshit.goswami.collegeapp.student.MainActivity
import android.app.NotificationManager as NotificationManager1


class FirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var notiStyle:NotificationCompat.Style
    private val CHANNEL_NAME = "FCM"
    private val CHANNEL_DESC = "Firebase Cloud Messaging"
    private var numMessages = 0

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
/*

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()

        // vibration
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 300, 300, 300)
        v.vibrate(pattern, -1)
*/
//
//        val resourceImage = resources.getIdentifier(
//            message.notification!!.icon, "drawable",
//            packageName
//        )
        if (message.data.isNotEmpty()) {
                    sendNotification(message.data)
            }
//        val notification: RemoteMessage.Notification = message.notification!!
//        val data: Map<String, String> = message.data
//        Log.d("FROM", message.from.toString())

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun sendNotification(data: Map<String, String>) {
//        val bundle = Bundle()
//        bundle.putString(FCM_PARAM, data[FCM_PARAM])
        val title = data["title"]
        val body = data["message"]
        val imgUrl = data["imageUrl"]
        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtras(bundle)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        when(data["type"]){
            "BIGPIC"->{
                notiStyle = NotificationCompat.BigPictureStyle()
                    .setBigContentTitle(title)
                    .setSummaryText(body)
                    .bigPicture(Glide.with(this@FirebaseMessagingService).asBitmap().load(imgUrl).submit().get())
            }
            "BIGTEXT"->{
                notiStyle = NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setSummaryText(title)
            }
            "BC"->{
                notiStyle = NotificationCompat.BigTextStyle()
                    .bigText(body)
                    .setSummaryText(title)
            }
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, this.getString(R.string.app_name))
                .setContentTitle(title)

                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(Uri.parse("android.resource://" + packageName + "/" + com.google.firebase.R.raw.firebase_common_keep))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setColor(Color.YELLOW)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notifications)
                .setStyle(
                    notiStyle
                )

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.app_name),//channel id
                CHANNEL_NAME,
                NotificationManager1.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}
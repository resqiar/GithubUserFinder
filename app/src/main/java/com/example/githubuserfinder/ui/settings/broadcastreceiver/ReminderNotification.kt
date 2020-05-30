package com.example.githubuserfinder.ui.settings.broadcastreceiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.githubuserfinder.R
import java.util.*

class ReminderNotification(): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context, 100)
    }

    private fun showNotification(context: Context, notifID: Int){

        val CHANNEL_NAME = "reminder_01"
        val CHANNEL_ID = "reminder_channel"

        // just create a notification like a normal way

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
            .setContentTitle(context.getString(R.string.reminder_notification))
            .setContentText(context.getString(R.string.reminder_notification_text))
            .setSound(notifSound)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notifID, notification)
    }

    fun getReminderTime(): Calendar{    // atur notif time ke 9 AM
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1)
        }

        return calendar
    }

    fun setReminderTime(context: Context){
        val intent = Intent(context, ReminderNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 100, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            getReminderTime().timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, "Reminder Set-up", Toast.LENGTH_SHORT).show()
    }

    fun cancelReminder(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderNotification::class.java)
        val requestCode = 100
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Reminder Dismissed", Toast.LENGTH_SHORT).show()
    }
}
package com.example.mediaplayer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.mediaplayer.MainActivity
import com.example.mediaplayer.R
import com.example.mediaplayer.model.Music

class MusicService : Service() {
    private var myBinder = MyBinder()
    var player: ExoPlayer? = null
    var musicList: List<Music> = mutableListOf()
    var songPosition: Int = -1

    companion object {
        const val CHANNEL_ID = "music_channel"
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    fun createMediaPlayer(position: Int) {
        try {
            val mediaItem = MediaItem.fromUri(musicList[position].path)
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.play()
            songPosition = position
            showNotification(R.drawable.ic_pause)
        } catch (e: Exception) {
            return
        }
    }

    fun playPause() {
        if (player?.isPlaying == true) {
            player?.pause()
            showNotification(R.drawable.ic_play)
        } else {
            player?.play()
            showNotification(R.drawable.ic_pause)
        }
    }

    private fun showNotification(playPauseBtn: Int) {
        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle(musicList[songPosition].title)
            .setContentText(musicList[songPosition].artist)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_music)) // Placeholder
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(contentIntent)
            .addAction(R.drawable.ic_previous, "Previous", null)
            .addAction(playPauseBtn, "Play", null)
            .addAction(R.drawable.ic_next, "Next", null)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}

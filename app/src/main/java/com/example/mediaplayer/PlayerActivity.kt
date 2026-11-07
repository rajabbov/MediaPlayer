package com.example.mediaplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.commit451.coiltransformations.BlurTransformation
import com.example.mediaplayer.databinding.ActivityPlayerBinding
import com.example.mediaplayer.service.MusicService

class PlayerActivity : AppCompatActivity(), ServiceConnection {

    private lateinit var binding: ActivityPlayerBinding
    private var musicService: MusicService? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)

        binding.backBtn.setOnClickListener { finish() }

        binding.playPauseBtn.setOnClickListener {
            musicService?.playPause()
            updateUIState()
        }

        binding.nextBtn.setOnClickListener {
            val nextPos = (musicService?.songPosition ?: 0) + 1
            if (nextPos < (musicService?.musicList?.size ?: 0)) {
                musicService?.createMediaPlayer(nextPos)
                updateUIState()
            }
        }

        binding.prevBtn.setOnClickListener {
            val prevPos = (musicService?.songPosition ?: 0) - 1
            if (prevPos >= 0) {
                musicService?.createMediaPlayer(prevPos)
                updateUIState()
            }
        }
    }

    private fun updateUIState() {
        val currentSong = musicService?.musicList?.get(musicService!!.songPosition)
        if (currentSong != null) {
            binding.playerTitle.text = currentSong.title
            binding.playerArtist.text = currentSong.artist
            
            // Immersive Background using Coil
            binding.backgroundBlur.load(currentSong.artUri) {
                error(R.drawable.gradient_bg)
                crossfade(true)
                transformations(BlurTransformation(this@PlayerActivity, 25f))
            }

            binding.playerArt.load(currentSong.artUri) {
                placeholder(R.drawable.ic_music)
                error(R.drawable.ic_music)
            }
            
            val isPlaying = musicService?.mediaPlayer?.isPlaying == true
            binding.playPauseBtn.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
            binding.visualizer.setPlaying(isPlaying)

            if (isPlaying) {
                val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
                binding.playerArt.startAnimation(rotate)
            } else {
                binding.playerArt.clearAnimation()
            }

            binding.circularSeekBar.max = musicService?.mediaPlayer?.duration ?: 0
            updateProgress()
        }
    }

    private fun updateProgress() {
        val currentPos = musicService?.mediaPlayer?.currentPosition ?: 0
        binding.circularSeekBar.progress = currentPos
        handler.postDelayed({ updateProgress() }, 500)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        updateUIState()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        unbindService(this)
    }
}

package com.example.mediaplayer

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.mediaplayer.adapter.MusicAdapter
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.example.mediaplayer.service.MusicService
import com.example.mediaplayer.viewmodel.MusicViewModel

class MainActivity : AppCompatActivity(), ServiceConnection {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MusicViewModel by viewModels()
    private var musicService: MusicService? = null
    private lateinit var musicAdapter: MusicAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.loadMusic()
            } else {
                Toast.makeText(this, "Neural Access Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayerScreen()
        }
    }

    private fun setupUI() {
        musicAdapter = MusicAdapter(emptyList()) { position ->
            musicService?.musicList = viewModel.musicList.value ?: emptyList()
            musicService?.createMediaPlayer(position)
            viewModel.setCurrentSong(musicService!!.musicList[position])
            viewModel.setPlaying(true)
        }
        binding.musicRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.musicRecyclerView.adapter = musicAdapter
    }

    private fun checkPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadMusic()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun observeViewModel() {
        viewModel.musicList.observe(this) { musicList ->
            musicAdapter.updateList(musicList)
        }

        viewModel.currentSong.observe(this) { song ->
            if (song != null) {
                binding.miniPlayer.visibility = View.VISIBLE
                binding.miniTitle.text = song.title
                binding.miniArtist.text = song.artist
                binding.miniArt.load(song.artUri) {
                    placeholder(R.drawable.ic_music)
                    error(R.drawable.ic_music)
                }
            }
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            binding.miniPlayPause.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(this)
    }
}

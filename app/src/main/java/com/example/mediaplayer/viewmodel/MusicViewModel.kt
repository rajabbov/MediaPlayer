package com.example.mediaplayer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediaplayer.model.Music
import com.example.mediaplayer.repository.MusicRepository

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MusicRepository(application)
    private val _musicList = MutableLiveData<List<Music>>()
    val musicList: LiveData<List<Music>> get() = _musicList

    private val _currentSong = MutableLiveData<Music?>()
    val currentSong: LiveData<Music?> get() = _currentSong

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    fun loadMusic() {
        _musicList.postValue(repository.getAllMusic())
    }

    fun setCurrentSong(music: Music) {
        _currentSong.value = music
    }

    fun setPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }
}

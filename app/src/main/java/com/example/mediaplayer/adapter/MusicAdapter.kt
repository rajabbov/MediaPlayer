package com.example.mediaplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.ItemMusicBinding
import com.example.mediaplayer.model.Music

class MusicAdapter(
    private var musicList: List<Music>,
    private val onMusicClick: (Int) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.binding.songTitle.text = music.title
        holder.binding.songArtist.text = music.artist
        
        holder.binding.songArt.load(music.artUri) {
            placeholder(R.drawable.ic_music)
            error(R.drawable.ic_music)
            crossfade(true)
        }

        holder.itemView.setOnClickListener {
            onMusicClick(position)
        }
    }

    override fun getItemCount(): Int = musicList.size

    fun updateList(newList: List<Music>) {
        musicList = newList
        notifyDataSetChanged()
    }
}

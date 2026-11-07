package com.example.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PlayerScreen(
    songTitle: String = "System Init Song",
    artistName: String = "Unknown Entity",
    albumArtUrl: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E1E2C), Color(0xFF0F0F1A))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Album Art (Glassmorphism Effect)
        Surface(
            modifier = Modifier
                .size(320.dp)
                .clip(RoundedCornerShape(32.dp)),
            color = Color.White.copy(alpha = 0.1f),
            tonalElevation = 8.dp
        ) {
            AsyncImage(
                model = albumArtUrl,
                contentDescription = "Album Art",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Song Info
        Text(
            text = songTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Text(
            text = artistName,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Progress Slider
        Slider(
            value = 0.4f,
            onValueChange = {},
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00FBFF),
                activeTrackColor = Color(0xFF00FBFF),
                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) { /* Shuffle Icon */ }
            IconButton(onClick = {}) { /* Previous Icon */ }
            
            FloatingActionButton(
                onClick = {},
                containerColor = Color(0xFF00FBFF)
            ) {
                // Play/Pause Icon
            }

            IconButton(onClick = {}) { /* Next Icon */ }
            IconButton(onClick = {}) { /* Repeat Icon */ }
        }
    }
}
package com.example.proyectoandroid;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity {


    @Override
    protected void
    onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String video = getIntent().getExtras().getString("idVideo");

        // Get reference to the view of Video player
        YouTubePlayerView ytPlayer = (YouTubePlayerView)findViewById(R.id.ytPlayer);

        ytPlayer.initialize(
                YoutubeAPI.getAPI(),
                new YouTubePlayer.OnInitializedListener() {
                    // Implement two methods by clicking on red
                    // error bulb inside onInitializationSuccess
                    // method add the video link or the playlist
                    // link that you want to play In here we
                    // also handle the play and pause
                    // functionality
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b)
                    {
                        youTubePlayer.loadVideo(video);
                        youTubePlayer.play();
                    }

                    // Inside onInitializationFailure
                    // implement the failure functionality
                    // Here we will show toast
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult)
                    {
                        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
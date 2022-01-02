package com.example.proyectoandroid.ui.youtube;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends YouTubeBaseActivity {
    private ListView lvVideos;
    private EditText tvSearch;
    private List<Video> videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);

        // Get reference to the view of Video player
        //YouTubePlayerView ytPlayer = (YouTubePlayerView)findViewById(R.id.ytPlayer);


        lvVideos = findViewById(R.id.lvVideos);
        tvSearch = findViewById(R.id.tvSearch);

    }

    public void iniciarBusqueda(View view) {
        //Lista con las ids
        List<String> busqueda = new ArrayList<>();
        /*Rellena aquí tu lista*/


        Buscador buscador = new Buscador(busqueda);

        buscador.start();
        try {
            buscador.join();
            videos = buscador.getVideos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        VideoListAdapter adaptador = new VideoListAdapter(this,videos);
        lvVideos.setAdapter(adaptador);

    }

    private class Buscador extends Thread {

        private List<String> busqueda;
        private List<Video> videos = null;

        public Buscador (List<String> busqueda) {
            this.busqueda = busqueda;
        }
        @Override
        public void run(){
            try {
                videos = YoutubeAPI.getListaVideos(busqueda);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<Video> getVideos() {
            return videos;
        }
    }
}
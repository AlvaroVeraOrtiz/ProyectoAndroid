package com.example.proyectoandroid.ui.youtube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class VideoSearchActivity extends AppCompatActivity {
    private ListView lvVideos;
    private EditText tvSearch;
    private List<SearchResult> videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);

        lvVideos = findViewById(R.id.lvVideos);
        tvSearch = findViewById(R.id.tvSearch);

        lvVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult res = (SearchResult) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("idVideo",res.getId().getVideoId());
                bundle.putString("titulo",res.getSnippet().getTitle());
                bundle.putString("descripcion",res.getSnippet().getDescription());
                bundle.putString("canal",res.getSnippet().getChannelTitle());
                bundle.putString("idCanal",res.getSnippet().getChannelId());

                Intent intento = new Intent(getApplicationContext(),VideoInfoActivity.class);
                intento.putExtras(bundle);
                startActivity(intento);

            }
        });


    }

    public void iniciarBusqueda(View view) {

        Buscador buscador = new Buscador(tvSearch.getText().toString());

        buscador.start();
        try {
            buscador.join();
            videos = buscador.getVideos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResultAdapter adaptador = new ResultAdapter(this,videos);
        lvVideos.setAdapter(adaptador);
    }

    private class Buscador extends Thread {

        private String busqueda;
        private List<SearchResult> videos = null;

        public Buscador (String busqueda) {
            this.busqueda = busqueda;
        }
        @Override
        public void run(){
            try {
                videos = YoutubeAPI.buscaVideos(busqueda);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<SearchResult> getVideos() {
            return videos;
        }
    }
}
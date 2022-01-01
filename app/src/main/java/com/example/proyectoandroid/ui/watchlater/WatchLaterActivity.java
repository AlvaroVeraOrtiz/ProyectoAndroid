package com.example.proyectoandroid.ui.watchlater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.example.proyectoandroid.ui.youtube.ListaActivity;
import com.example.proyectoandroid.ui.youtube.ResultAdapter;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {
    private ListView wlVideos;
    private List<SearchResult> videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        wlVideos = findViewById(R.id.wlVideos); //obtener el elemento de la interfaz
        mostrarVideos();
    }

    public void mostrarVideos() //View view
    {
        //Lista con las ids
        List<String> busqueda = new ArrayList<>();
        /*Rellena aquí tu lista*/
        busqueda.add("Pf5Gid4RhJ8");
        busqueda.add("H57FTLsOI6E");
        busqueda.add("h1fd2S3gBww");
        //busqueda.add("SM3nn__Fo04");
        busqueda.add("4Bv45aPMGyI");
        busqueda.add("wFGopqS4g4g");
        busqueda.add("I70Xxh-EbAs");

        WatchLaterActivity.Buscador buscador = new WatchLaterActivity.Buscador(busqueda);

        buscador.start();
        try {
            buscador.join();
            videos = buscador.getVideos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResultAdapter adaptador = new ResultAdapter(this,videos);
        wlVideos.setAdapter(adaptador);
    }

    private class Buscador extends Thread {

        private List<String> busqueda;
        private List<SearchResult> videos = null;

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

        public List<SearchResult> getVideos() {
            return videos;
        }
    }
}
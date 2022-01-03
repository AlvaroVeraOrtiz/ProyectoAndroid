package com.example.proyectoandroid.ui.watchlater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.example.proyectoandroid.ui.youtube.ResultAdapter;
import com.example.proyectoandroid.ui.youtube.VideoInfoActivity;
import com.example.proyectoandroid.ui.youtube.VideoListAdapter;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {
    private ListView wlVideos;
    private List<Video> videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        wlVideos = findViewById(R.id.wlVideos); //obtener el elemento de la interfaz
        mostrarVideos();

        wlVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video res = (Video) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("idVideo",res.getId());
                bundle.putString("titulo",res.getSnippet().getTitle());
                bundle.putString("descripcion",res.getSnippet().getDescription());
                bundle.putString("canal",res.getSnippet().getChannelTitle());
                bundle.putString("idCanal",res.getSnippet().getChannelId());

                Intent intento = new Intent(getApplicationContext(), VideoInfoActivity.class);
                intento.putExtras(bundle);
                startActivity(intento);

            }
        });
    }

    public void mostrarVideos() //View view
    {
        //Lista con las ids
        List<String> busqueda = new ArrayList<>();
        /*Rellena aquí tu lista*/

        SingletonMap sm = SingletonMap.getInstance();
        Usuario usuario = (Usuario) sm.get("usuario");
        if(usuario!= null && usuario.getVerMasTarde()!=null){
            for(String v: usuario.getVerMasTarde()){
                busqueda.add(v);
            }
        }

        WatchLaterActivity.Buscador buscador = new WatchLaterActivity.Buscador(busqueda);

        buscador.start();
        try {
            buscador.join();
            videos = buscador.getVideos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        VideoListAdapter adaptador = new VideoListAdapter(this,videos);
        wlVideos.setAdapter(adaptador);
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
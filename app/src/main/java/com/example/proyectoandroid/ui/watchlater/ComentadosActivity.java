package com.example.proyectoandroid.ui.watchlater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.example.proyectoandroid.ui.youtube.VideoInfoActivity;
import com.example.proyectoandroid.ui.youtube.VideoListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.model.Video;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComentadosActivity extends AppCompatActivity {
    private ListView wlVideos;
    private List<Video> videos;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        Intent intento = getIntent();
        idUsuario = intento.getExtras().getString("idUsuario");

        if(idUsuario==null || idUsuario.isEmpty()){
            Usuario u = (Usuario) SingletonMap.getInstance().get("usuario");
            idUsuario = u.getUid();
        }

        wlVideos = findViewById(R.id.wlVideos); //obtener el elemento de la interfaz
        cargarVideos();

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

    private void cargarVideos() //View view
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mensajes")
                .whereEqualTo("creador",idUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Set<String> comentados = new HashSet<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String v = document.get("video",String.class);
                                comentados.add(v);
                            }
                            mostrarVideos(comentados);

                        }else{
                            Toast.makeText(ComentadosActivity.this,getString(R.string.sincronizacion_fallida), Toast.LENGTH_LONG).show();
                        }
                        db.terminate();
                    }
                });
    }

    private void mostrarVideos(Set<String> comentados){
        List<String> busqueda = new ArrayList<>();


        if(comentados!= null){
            for(String v: comentados){
                busqueda.add(v);
            }
        }

        ComentadosActivity.Buscador buscador = new ComentadosActivity.Buscador(busqueda);

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
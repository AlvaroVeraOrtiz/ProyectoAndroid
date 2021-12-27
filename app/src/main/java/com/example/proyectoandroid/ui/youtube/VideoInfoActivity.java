package com.example.proyectoandroid.ui.youtube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectoandroid.PlayerActivity;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.google.api.services.youtube.model.Channel;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class VideoInfoActivity extends AppCompatActivity {

    private TextView tvTitulo;
    private TextView tvCanal;
    private ImageButton btnMiniatura;
    private ImageButton btnCanal;
    //private FloatingActionButton btnCanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        Intent intento = getIntent();
        String idVideo = intento.getExtras().getString("idVideo");
        String titulo = intento.getExtras().getString("titulo");
        String descripcion = intento.getExtras().getString("descripcion");
        String canal = intento.getExtras().getString("canal");
        String idCanal = intento.getExtras().getString("idCanal");

        tvTitulo = findViewById(R.id.tvTitulo);
        btnMiniatura = findViewById(R.id.btnMiniatura);
        btnCanal = findViewById(R.id.btnCanal);
        tvCanal = findViewById(R.id.tvCan);
        Glide.with(getApplicationContext()).load("https://img.youtube.com/vi/" + idVideo + "/0.jpg").into(btnMiniatura);
        tvTitulo.setText(titulo);
        tvCanal.setText(canal);

        try {
            Buscador b = new Buscador(idCanal);
            b.start();
            b.join();
            String url = b.getCanal().getSnippet().getThumbnails().getDefault().getUrl();
            Glide.with(getApplicationContext()).load(url).into(btnCanal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btnMiniatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(getApplicationContext(), PlayerActivity.class);
                intento.putExtra("idVideo",idVideo);
                startActivity(intento);
            }
        });

    }

    private class Buscador extends Thread {

        private String busqueda;
        private Channel canal = null;

        public Buscador (String busqueda) {
            this.busqueda = busqueda;
        }
        @Override
        public void run(){
            try {
                canal = YoutubeAPI.getCanal(busqueda);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Channel getCanal() {
            return canal;
        }
    }
}
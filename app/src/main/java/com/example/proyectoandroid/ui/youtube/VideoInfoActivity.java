package com.example.proyectoandroid.ui.youtube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.model.Channel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class VideoInfoActivity extends AppCompatActivity {

    private TextView tvTitulo;
    private TextView tvCanal;
    private ImageButton btnMiniatura;
    private ImageButton btnCanal;
    private Button btnVerMasTarde;
    private Usuario usuario;
    private String idVideo;
    //private FloatingActionButton btnCanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        Intent intento = getIntent();
        idVideo = intento.getExtras().getString("idVideo");
        String titulo = intento.getExtras().getString("titulo");
        String descripcion = intento.getExtras().getString("descripcion");
        String canal = intento.getExtras().getString("canal");
        String idCanal = intento.getExtras().getString("idCanal");

        SingletonMap sm = SingletonMap.getInstance();
        usuario = (Usuario) sm.get("usuario");
        btnVerMasTarde = findViewById(R.id.btnVerMasTarde);
        if(usuario.getVerMasTarde()!=null && usuario.getVerMasTarde().contains(idVideo)){
            btnVerMasTarde.setText(getResources().getString(R.string.deleteVerMasTarde));
        }


        tvTitulo = findViewById(R.id.tvTitulo);
        btnMiniatura = findViewById(R.id.btnMiniatura);
        btnCanal = findViewById(R.id.btnCanal);
        tvCanal = findViewById(R.id.tvCan);
        Glide.with(getApplicationContext()).load("https://img.youtube.com/vi/" + idVideo + "/0.jpg").into(btnMiniatura);

        Spanned s = Html.fromHtml(titulo, HtmlCompat.FROM_HTML_MODE_LEGACY);
        tvTitulo.setText(s);

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

    public void addDeleteVerMasTarde(View view) {
        btnVerMasTarde.setEnabled(false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("usuarios")
                                    .document(usuario.getUid());

        if(usuario.getVerMasTarde()==null){
            List<String> array = new ArrayList<>();
            array.add(idVideo);

            doc.update("verMasTarde",array)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                usuario.setVerMasTarde(array);
                                btnVerMasTarde.setText(getResources().getString(R.string.deleteVerMasTarde));

                            }else{
                                Toast.makeText(VideoInfoActivity.this, getString(R.string.sincronizacion_fallida), Toast.LENGTH_LONG).show();

                            }
                            btnVerMasTarde.setEnabled(true);
                        }
                    });

        }else{
            if(usuario.getVerMasTarde().contains(idVideo)){
                doc.update("verMasTarde", FieldValue.arrayRemove(idVideo))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    usuario.getVerMasTarde().remove(idVideo);
                                    btnVerMasTarde.setText(getResources().getString(R.string.addVerMasTarde));

                                }else{
                                    Toast.makeText(VideoInfoActivity.this, getString(R.string.sincronizacion_fallida), Toast.LENGTH_LONG).show();

                                }
                                btnVerMasTarde.setEnabled(true);
                            }
                        });

            }else{

                doc.update("verMasTarde", FieldValue.arrayUnion(idVideo))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    usuario.getVerMasTarde().add(idVideo);
                                    btnVerMasTarde.setText(getResources().getString(R.string.deleteVerMasTarde));

                                }else{
                                    Toast.makeText(VideoInfoActivity.this, getString(R.string.sincronizacion_fallida), Toast.LENGTH_LONG).show();

                                }
                                btnVerMasTarde.setEnabled(true);
                            }
                        });
            }
        }
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
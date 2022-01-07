package com.example.proyectoandroid.ui.youtube;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.Mensajes;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.example.proyectoandroid.ui.mensajes.MensajesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerActivity extends YouTubeBaseActivity {

    private FirebaseFirestore db;

    private Usuario usuario;
    private List<Mensajes> mensajes;
    private MensajesAdapter adapter;
    ListenerRegistration registration;
    private ListView chat;
    private YouTubePlayer player = null;
    private String current = "";
    private EditText texto;
    private Button enviar;
    private Handler handler = new Handler();

    private int t1 = 0;
    private int t2 = 0;

    @Override
    protected void
    onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        db = FirebaseFirestore.getInstance();

        texto = findViewById(R.id.mensaje);
        enviar = findViewById(R.id.enviar_mensaje);
        chat = findViewById(R.id.listaDeMensajes);

        mensajes = new ArrayList<>();
        adapter = new MensajesAdapter(this,R.id.listaDeMensajes,mensajes);
        chat.setAdapter(adapter);

        findViewById(R.id.enviar_mensaje).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearMensaje();
            }
        });

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

                        player = youTubePlayer;
                        current = video;

                        mensajes  = new ArrayList<>();
                        cargarSeguidos(video);
                    }

                    // Inside onInitializationFailure
                    // implement the failure functionality
                    // Here we will show toast
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult)
                    {

                        new AlertDialog.Builder(PlayerActivity.this)
                                .setTitle(getString(R.string.video_fallido))
                                .setMessage(getString(R.string.video_fallido))
                                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        finish();
                                    }
                                })
                                .show();
                    }
                });



    }

    private void cargarSeguidos(String video){
        /*
         *  Función que toma carga el id del usuario y devuelve los ids de los que sigue.
         */
        SingletonMap sm = SingletonMap.getInstance();
        usuario = (Usuario) sm.get("usuario");

        //Buscamos los usuarios seguidos y devolvemos sus ids.
        db.collection("usuarios")
                .document(usuario.getUid())
                .collection("seguidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> res = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                res.add(document.getId());
                            }

                            res.add(usuario.getUid());

                            //Una vez que tenemos los usuarios seguidos cargamos los mensajes
                            //de esos usuarios en el video.
                            cargarMensajesBD(res, video);


                        }else{
                            new AlertDialog.Builder(PlayerActivity.this)
                                    .setTitle(getString(R.string.error_mensajes))
                                    .setMessage(getString(R.string.error_mensajes))
                                    .setNegativeButton(android.R.string.ok,null)
                                    .show();
                        }
                    }
                });
    }

    public void cargarMensajesBD(List<String> seguidos, String url){
        /*
         * Creamos un EventListener sobre la BD, esto nos proporciona los mensajes ya existentes
         * y aquellos que se crean mientras el usuario está viendo el video.
         */
         registration = db.collection("mensajes")
                                    .whereEqualTo("video",url)
                                    .whereIn("creador",seguidos)
                                    .orderBy("momento", Query.Direction.ASCENDING)
                                    .addSnapshotListener(
                                            new EventListener<QuerySnapshot>() {

                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.w("listen:error", error);
                                                        return;
                                                    }

                                                    for (DocumentChange dc : value.getDocumentChanges()) {
                                                        if (dc.getType().equals(DocumentChange.Type.ADDED)) {
                                                            //Si el evento es un mensaje que se ha añadido lo añadimos
                                                            //a la lista

                                                            Mensajes m = dc.getDocument().toObject(Mensajes.class);
                                                            if(m.getMomento()<t2){
                                                                adapter.add(m);
                                                            }
                                                            mensajes.add(m);
                                                        }
                                                    }
                                                }
                                            });
         //Una vez cargados los mensajes ejecutamos una tarea periodica que añade los mensajes
         //que tocan en ese momento a la vista.
         ejecutarTarea();

    }



    public void crearMensaje(){
        //Esta función es la que se ejecutará cuando se pulse enviar en la actividad.
        //Se extrae el texto y si no es nulo se que cargan los datos en la actividad.
        String text = texto.getText().toString();
        if(!TextUtils.isEmpty(text) && player!=null && !current.equals("")){
            enviar.setEnabled(false);

            int momento = player.getCurrentTimeMillis();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> mensaje = new HashMap<>();

            mensaje.put("momento", momento);
            mensaje.put("creador", usuario.getUid());
            mensaje.put("nombreCreador",usuario.getNombre());
            mensaje.put("contenido",text);
            mensaje.put("video",current);

            db.collection("mensajes").document()
                    .set(mensaje)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PlayerActivity.this, getString(R.string.envio_exitoso), Toast.LENGTH_SHORT).show();
                            enviar.setEnabled(true);
                            texto.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PlayerActivity.this, getString(R.string.envio_fallido), Toast.LENGTH_SHORT).show();
                            enviar.setEnabled(true);
                        }
                    });

        }
    }

    //Tarea que se ejecuta de forma periodica y tiene como objetivo mostrar solo los comentarios
    //que deberían salir entre el momento anterior del video y el actual.
    public void ejecutarTarea() {
        handler.postDelayed(new Runnable() {
            public void run() {

                // función a ejecutar
                aplicarFiltro(); // función para refrescar la lista

                handler.postDelayed(this, 1000);
            }

        }, 1000);

    }

    /*
     * En lugar de ser un filtro dentro del adapter,
     * hemos considerado que es mejor añadir solo los mensajes nuevos entre tiempos de ejecución
     * para reducir el número de veces que hay que refrescar la lista.
     */
    private void aplicarFiltro() {
        if(player!=null){

            t1 = t2;
            try{
                t2 = player.getCurrentTimeMillis();
            }catch (IllegalStateException e){
                Log.d("Yt player","Cerró antes de tiempo");

            }

            filtrar();
        }
    }

    public void filtrar(){
        /*
         * Añadimos al adapte los mensajes que ocurren entre el instante anterior del video
         * y el actual.
         */

        if(t1>t2){
            adapter.clear();
            Collections.sort(mensajes);
            t1 = 0;
        }

        for(Mensajes m : mensajes){
            if(t1<=m.getMomento() && m.getMomento()<t2){
                adapter.add(m);
            }
        }

    }
    @Override
    public void onDestroy() {
        /*
         * Cerramos el player, el listener de la base de datos y
         * la tarea periodica de esta actividad.
         */

        if (player != null) {
            player.release();
        }
        if(registration!=null){
            registration.remove();
        }
        handler.removeCallbacksAndMessages(null);
        db.terminate();
        super.onDestroy();
    }

}
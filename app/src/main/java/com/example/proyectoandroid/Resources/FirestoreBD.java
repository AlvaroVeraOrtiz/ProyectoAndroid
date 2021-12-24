package com.example.proyectoandroid.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import javax.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;



public class FirestoreBD {
    private final FirebaseFirestore db;

    public FirestoreBD(){
        this.db = FirebaseFirestore.getInstance();
    }


    public void idsSiguiendo(String idUsuario, List<String> res){
        /**
         *  Función que toma el id del usuario y devuelve los ids de los que sigue.
         */

        //Buscamos los usuarios seguidos y devolvemos sus ids.

        db.collection("usuarios")
                .document("VBZqjliJ98a0pXoGRsjY")
                .collection("seguidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                res.add(document.getId());
                            }
                        } else {
                            res.add("Error");
                        }
                    }
                });
    }
    public EventListener<QuerySnapshot> listenToMensajes(String url, String idUsuario, List<Mensajes> mensajes) {
        /**
         * Función que escucha a la tabla mensajes y va añadiendo a la lista pasada
         * mensajes que son del video y de un usuario que sigue el usuario activo.
         */

        //Obtenemos usuarios seguidos por el usuario
        List<String> seguidos = new ArrayList<>();

        //Creamos un Listener que se encargará de añadir los mensajes nuevos.
        EventListener<QuerySnapshot> listener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //Tratar el error
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    if(dc.getType() == DocumentChange.Type.ADDED) {
                        //Añadimos el mensaje a la lista de mensajes
                        mensajes.add(dc.getDocument().toObject(Mensajes.class));
                    }
                }

            }
        };

        //Hacemos la consulta que obtiene mensajes de seguidos en el video con el Listener
        db.collection("mensajes")
                .whereEqualTo("video", url)
                .whereIn("usuario",seguidos)
                .addSnapshotListener(listener);

        // Devolvemos el listener para que se pueda cerrar
        return listener;
    }

}

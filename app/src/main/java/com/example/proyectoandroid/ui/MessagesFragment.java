package com.example.proyectoandroid.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.Mensajes;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "url";

    // TODO: Rename and change types of parameters
    private String url;
    private Usuario usuario;
    private List<Mensajes> mensajes;

    public MessagesFragment() {
        // Required empty public constructor
        SingletonMap sm = SingletonMap.getInstance();
        usuario = (Usuario) sm.get("usuario");
        mensajes = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }
    public void idsSiguiendo(String idUsuario){
        /**
         *  Función que toma el id del usuario y devuelve los ids de los que sigue.
         */

        //Buscamos los usuarios seguidos y devolvemos sus ids.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            cargarMensajesBD(res);
                        }
                    }
                });
    }
    public void cargarMensajesBD(List<String> seguidos){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mensajes")
                .whereEqualTo("video",url)
                .whereIn("creador",seguidos)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mensajes.add(document.toObject(Mensajes.class));
                            }
                        } else {
                            Log.d("Error getting documents: ", task.getException().toString());
                        }
                    }
                });

    }
}
package com.example.proyectoandroid.ui.seguidos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.FirestoreBD;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.model.SearchResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeguidosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeguidosFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FirebaseFirestore db;
    UsuarioAdapter listViewAdapter;


    public SeguidosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SeguidosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeguidosFragment newInstance() {
        SeguidosFragment fragment = new SeguidosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seguidos, container, false);


        ArrayList<Usuario> seguidosItems = new ArrayList<>();
        ListView seguidosView = (ListView) view.findViewById(R.id.seguidosListView);

        listViewAdapter = new UsuarioAdapter(
                getActivity(),
                R.id.seguidosListView,
                seguidosItems
        );

        seguidosView.setAdapter(listViewAdapter);
        idsSiguiendo();
        return view;
    }

    public void idsSiguiendo(){
        /**
         *  Función que toma el id del usuario y devuelve los ids de los que sigue.
         */
        SingletonMap sm = SingletonMap.getInstance();
        Usuario u = (Usuario) sm.get("usuario");
        //Buscamos los usuarios seguidos y devolvemos sus ids.
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(u.getUid())
                .collection("seguidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listViewAdapter.add(document.toObject(Usuario.class));
                            }
                        }
                    }
                });
    }
}
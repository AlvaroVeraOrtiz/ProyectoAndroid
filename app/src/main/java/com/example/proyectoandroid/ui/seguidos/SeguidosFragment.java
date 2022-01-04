package com.example.proyectoandroid.ui.seguidos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.proyectoandroid.R;
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
 * Use the {@link SeguidosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeguidosFragment extends Fragment {



    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FirebaseFirestore db;
    private ListView seguidosView;
    private UsuarioAdapter listViewAdapter;


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


        seguidosView = (ListView) view.findViewById(R.id.seguidosListView);


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
                            List<Usuario> seguidosItems = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario u = document.toObject(Usuario.class);
                                u.setUid(document.getId());
                                seguidosItems.add(u);
                            }
                            listViewAdapter = new UsuarioAdapter(
                                    getActivity(),
                                    R.id.seguidosListView,
                                    seguidosItems,
                                    seguidosItems
                            );

                            seguidosView.setAdapter(listViewAdapter);
                        }
                        db.terminate();
                    }
                });
    }
}
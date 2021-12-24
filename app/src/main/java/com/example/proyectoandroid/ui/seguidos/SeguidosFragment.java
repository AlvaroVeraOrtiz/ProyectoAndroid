package com.example.proyectoandroid.ui.seguidos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.FirestoreBD;
import com.example.proyectoandroid.Resources.YoutubeAPI;
import com.google.api.services.youtube.model.SearchResult;

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
    private FirestoreBD db = new FirestoreBD();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<String> usuariosSeguidos;// = (ArrayList<String>) db.idsSiguiendo("VBZqjliJ98a0pXoGRsjY");

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeguidosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SeguidosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeguidosFragment newInstance(String param1, String param2) {
        SeguidosFragment fragment = new SeguidosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        Buscador buscador = new Buscador("VBZqjliJ98a0pXoGRsjY");
        buscador.start();
        try {
            buscador.join();
            usuariosSeguidos = buscador.getSeguidos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object[] objectArray = usuariosSeguidos.toArray(); //{"Junko", "Morgana"};
        String[] seguidosItems = Arrays.copyOf(objectArray, objectArray.length, String[].class); //{"Junko", "Morgana"};

        ListView seguidosView = (ListView) view.findViewById(R.id.seguidosListView);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                seguidosItems
        );

        seguidosView.setAdapter(listViewAdapter);
        return view;
    }

    private class Buscador extends Thread {

        private String busqueda;
        private List<String> seguidos = null;

        public Buscador (String busqueda) {
            this.busqueda = busqueda;
        }
        @Override
        public void run(){
            try {
                seguidos = (ArrayList<String>) db.idsSiguiendo(busqueda);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<String> getSeguidos() {
            return seguidos;
        }
    }
}
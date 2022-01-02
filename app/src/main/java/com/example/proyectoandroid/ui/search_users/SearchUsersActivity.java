package com.example.proyectoandroid.ui.search_users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.ui.youtube.ListaActivity;
import com.example.proyectoandroid.ui.youtube.VideoListAdapter;
import com.google.api.services.youtube.model.Video;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersActivity extends AppCompatActivity
{
    private ListView lvUsuarios;
    private EditText tvSearch;
    private List<Video> videos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        lvUsuarios = findViewById(R.id.lvUsuarios);
        tvSearch = findViewById(R.id.tvSearch);
    }

    public void iniciarBusqueda(View view) {
        //Lista con las ids
        List<String> busqueda = new ArrayList<>();
        /*Rellena aquí tu lista*/

    }
}
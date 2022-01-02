package com.example.proyectoandroid.ui.search_users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.example.proyectoandroid.ui.seguidos.UsuarioAdapter;
import com.example.proyectoandroid.ui.youtube.ListaActivity;
import com.example.proyectoandroid.ui.youtube.VideoListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.model.Video;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchUsersActivity extends AppCompatActivity
{
    private ListView lvUsuarios;
    private UsuarioAdapter listViewAdapter;
    private EditText tvSearch;
    private List<Usuario> usuarios = null;
    private List<Usuario> seguidos = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        tvSearch = findViewById(R.id.tvSearch);
        lvUsuarios = findViewById(R.id.lvUsuarios);
        tvSearch = findViewById(R.id.tvSearch);
    }

    public void iniciarBusqueda(View view) {

        if(seguidos==null || usuarios==null){
            idsSiguiendo();
        }else{
            listViewAdapter.clear();
            String mail = tvSearch.getText().toString();
            for(Usuario u : usuarios){
                if(u.getEmail().contains(mail)){
                    listViewAdapter.add(u);
                }
            }
        }


    }

    private void idsSiguiendo(){
        /**
         *  Función que toma el id del usuario y devuelve los ids de los que sigue.
         */
        SingletonMap sm = SingletonMap.getInstance();
        Usuario u = (Usuario) sm.get("usuario");
        //Buscamos los usuarios seguidos y devolvemos sus ids.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(u.getUid())
                .collection("seguidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            seguidos = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario u = document.toObject(Usuario.class);
                                u.setUid(document.getId());
                                Log.d("Seguidos",u.getEmail());
                                seguidos.add(u);
                            }
                            usuarios();

                        }
                    }
                });
    }

    private void usuarios(){
        SingletonMap sm = SingletonMap.getInstance();
        Usuario u = (Usuario) sm.get("usuario");
        //Buscamos los usuarios seguidos y devolvemos sus ids.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereNotEqualTo("email",u.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            usuarios = new ArrayList<>();
                            ArrayList<Usuario> res = new ArrayList<>();
                            String mail = tvSearch.getText().toString();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario u = document.toObject(Usuario.class);
                                u.setUid(document.getId());

                                if(u.getEmail().contains(mail)){
                                    res.add(u);
                                }
                                Log.d("Usuarios",u.getEmail());
                                usuarios.add(u);
                            }

                            listViewAdapter = new UsuarioAdapter(
                                    SearchUsersActivity.this,
                                    R.id.lvUsuarios,
                                    res,
                                    seguidos
                            );



                            lvUsuarios.setAdapter(listViewAdapter);
                        }
                        db.terminate();
                    }
                });
    }



}
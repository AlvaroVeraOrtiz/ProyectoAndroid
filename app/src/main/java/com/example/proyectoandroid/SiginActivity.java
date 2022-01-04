package com.example.proyectoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SiginActivity extends AppCompatActivity {
    private EditText nombre, correo, password;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);
        nombre = findViewById(R.id.nombreDeRegistro);
        correo = findViewById(R.id.correoDeRegistro);
        password = findViewById(R.id.passwordDeRegistro);
    }

    public void crearCuenta(View view) {
        String email = correo.getText().toString();
        String pass = password.getText().toString();
        String nom = nombre.getText().toString();

        if(TextUtils.isEmpty(nom)){
            Toast.makeText(this,getString(R.string.nombre_requerido), Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this,getString(R.string.datos_no_introducidos), Toast.LENGTH_LONG).show();
        }else if(pass.length()<6){
            Toast.makeText(this,getString(R.string.contreseña_no_valida), Toast.LENGTH_LONG).show();
        }else {

            mauth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        guardarUsuarioBD(task.getResult().getUser().getUid(),email,nom);

                    } else {
                        Toast.makeText(SiginActivity.this, getString(R.string.registro_fallido), Toast.LENGTH_LONG).show();
                    }


                }


            });
        }
    }
    private void guardarUsuarioBD(String uid, String email, String nom){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuario = new HashMap<>();
        usuario.put("nombre",nom);
        usuario.put("email",email);

        db.collection("usuarios").document(uid).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SiginActivity.this, getString(R.string.guardando_datos), Toast.LENGTH_LONG).show();
                    crearUsuario(uid, email, nom);
                }else{
                    Toast.makeText(SiginActivity.this, getString(R.string.guardado_fallido), Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    private void crearUsuario(String uid, String email, String nom) {
        Toast.makeText(SiginActivity.this, getString(R.string.registro_exitoso), Toast.LENGTH_LONG).show();
        SingletonMap sm = SingletonMap.getInstance();
        Usuario us = new Usuario(uid,email,nom);
        sm.put("usuario",us);
        startActivity(new Intent(SiginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SiginActivity.this,LoginActivity.class));
        finish();
    }

}
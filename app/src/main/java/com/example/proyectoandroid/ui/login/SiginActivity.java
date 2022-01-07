package com.example.proyectoandroid.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoandroid.MainActivity;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
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
        /*
         * Crea la cuenta con los datos introducidos.
         * En caso de ser datos no válidos se notifica al usuario.
         */
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
                        //Una vez registrado en el apartado de autenticación, se guardan en la BD.
                        guardarUsuarioBD(task.getResult().getUser().getUid(),email,nom);

                    } else {
                        Toast.makeText(SiginActivity.this, getString(R.string.registro_fallido), Toast.LENGTH_LONG).show();
                    }


                }


            });
        }
    }
    private void guardarUsuarioBD(String uid, String email, String nom){
        /*
         * Esta función guarda los datos del usuario en la BD.
         * El procedimiento en Firestore es siempre el mismo, se crea un map
         * que representa el objeto y sus atributos y estos se guardan en un documento.
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuario = new HashMap<>();
        usuario.put("nombre",nom);
        usuario.put("email",email);

        db.collection("usuarios").document(uid).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Una vez guardados los datos en la BD se crea el usuario en local
                    Toast.makeText(SiginActivity.this, getString(R.string.guardando_datos), Toast.LENGTH_LONG).show();
                    crearUsuario(uid, email, nom);
                }else{
                    Toast.makeText(SiginActivity.this, getString(R.string.guardado_fallido), Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    private void crearUsuario(String uid, String email, String nom) {
        /*
         * Esta función crea una copia local del usuario que se almacena en
         * el SingletonMap para posteriormente poder usar esos datos en el
         * resto de la aplicación.
         */
        Toast.makeText(SiginActivity.this, getString(R.string.registro_exitoso), Toast.LENGTH_LONG).show();

        //Se consigue la instancia del SingletonMap y se inserta el usuario en él.
        SingletonMap sm = SingletonMap.getInstance();
        Usuario us = new Usuario(uid,email,nom);
        sm.put("usuario",us);

        //Una vez guardado el usuario en el SingletonMap se pasa a la actividad principal.
        startActivity(new Intent(SiginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        /*
         * Reescribimos el comportamiento de pulsar hacia atrás.
         * Si no lo hicieramos se cerraría la app al pulsar hacia atrás en el registro,
         * dado que no queremos tener la actividad de login y de registro activas a la vez.
         */
        startActivity(new Intent(SiginActivity.this,LoginActivity.class));
        finish();
    }

}
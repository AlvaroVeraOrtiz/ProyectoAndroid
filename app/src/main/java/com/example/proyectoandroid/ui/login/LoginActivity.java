package com.example.proyectoandroid.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoandroid.MainActivity;
import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText correo, password;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!TextUtils.isEmpty(mauth.getUid())){
            sacarUsuarioPorId(mauth.getUid());
        }else{
            setContentView(R.layout.activity_login);
            correo = findViewById(R.id.correoDeUsuario);
            password = findViewById(R.id.passwordDeUsuario);
        }

    }

    public void iniciarSesion(View view) {
        /*
         * Esta función se llama para comprobar si el email y la contraseña están regitrados.
         * En caso de ser datos válidos se recupera la información del usuario.
         */

        String email = correo.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this,getString(R.string.datos_no_introducidos), Toast.LENGTH_LONG).show();
        }else if(pass.length()<6){
            Toast.makeText(this,getString(R.string.contreseña_no_valida), Toast.LENGTH_LONG).show();
        }else{
            //Llamamos a la función de autenticación de Firebase
            mauth.signInWithEmailAndPassword(email,pass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //Si los datos son correctos se recupera el usuario de la BD.
                            Toast.makeText(LoginActivity.this,getString(R.string.inicio_sesion_exitoso), Toast.LENGTH_LONG).show();
                            Log.d("ID_Usuario",authResult.getUser().getUid());
                            sacarUsuarioPorId(authResult.getUser().getUid());
                        }

                    } )
                    .addOnFailureListener(new OnFailureListener(){
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = getString(R.string.inicio_sesion_fallido);
                            if(e instanceof FirebaseAuthException){
                                FirebaseAuthException ex = (FirebaseAuthException) e;
                                String code = ex.getErrorCode();
                                if("ERROR_WRONG_PASSWORD".equals(code)){
                                    error = getString(R.string.contraseña_incorrecta);
                                }else if("ERROR_USER_NOT_FOUND".equals(code)){
                                    error = getString(R.string.usuario_incorrecto);
                                }else if("ERROR_INVALID_EMAIL".equals(code)){
                                    error = getString(R.string.correo_invalido);
                                }
                            }

                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle(getString(R.string.error_inicio))
                                    .setMessage(error)
                                    .setNegativeButton(android.R.string.ok, null)
                                    .show();


                        }
                    } );
        }
    }

    public void sacarUsuarioPorId(String uid){
        /*
         * Esta función recupera al usuario de la BD en base al id.
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("usuarios").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario user = documentSnapshot.toObject(Usuario.class);

                if(user!=null){
                    Toast.makeText(LoginActivity.this,getString(R.string.recuperando_datos), Toast.LENGTH_LONG).show();

                    user.setUid(uid);
                    SingletonMap sm = SingletonMap.getInstance();
                    sm.put("usuario",user);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,getString(R.string.recuperacion_fallida), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void abrirRegistrarse(View view) {
        /*
         * Función que se ejecuta cuando se pulsa en el botón de crear cuenta.
         * Abre la pestaña de registro.
         */
        startActivity(new Intent(LoginActivity.this,SiginActivity.class));
        finish();
    }



}
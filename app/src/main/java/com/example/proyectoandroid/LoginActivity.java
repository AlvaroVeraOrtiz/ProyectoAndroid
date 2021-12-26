package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoandroid.ui.login.LoginFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText correo, password;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo = findViewById(R.id.correoDeUsuario);
        password = findViewById(R.id.passwordDeUsuario);
    }

    public void iniciarSesion(View view) {
        String email = correo.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this,getString(R.string.datos_no_introducidos), Toast.LENGTH_LONG).show();
        }else if(pass.length()<6){
            Toast.makeText(this,getString(R.string.contreseña_no_valida), Toast.LENGTH_LONG).show();
        }else{

            mauth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this,getString(R.string.inicio_sesion_exitoso), Toast.LENGTH_LONG).show();
                    Log.d("ID_Usuario",authResult.getUser().getUid());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }


            } );
        }
    }

    public void iniciarUsuario(){

    }

    public void abrirRegistrarse(View view) {
        startActivity(new Intent(LoginActivity.this,SiginActivity.class));
        finish();
    }



}
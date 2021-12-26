package com.example.proyectoandroid.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoandroid.LoginActivity;
import com.example.proyectoandroid.MainActivity;
import com.example.proyectoandroid.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "auth";


    private EditText correo, password;
    private FirebaseAuth mauth= FirebaseAuth.getInstance();

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static LoginFragment newInstance(FirebaseAuth auth) {
        LoginFragment fragment = new LoginFragment();
        fragment.setAuth(auth);
        return fragment;
    }

    public void setAuth(FirebaseAuth auth) {
        mauth = auth;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        correo = view.findViewById(R.id.correoDeUsuario);
        password = view.findViewById(R.id.passwordDeUsuario);
        view.findViewById(R.id.inicioSesion)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iniciarSesion();
                        }
                    });
        view.findViewById(R.id.crearCuenta)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cargarRegistro();
                    }
                });

        return view;
    }

    public void iniciarSesion() {

        String email = correo.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this.getContext(),getString(R.string.datos_no_introducidos), Toast.LENGTH_LONG).show();
        }else if(pass.length()<6){
            Toast.makeText(this.getContext(),getString(R.string.contreseña_no_valida), Toast.LENGTH_LONG).show();
        }else{

            mauth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginFragment.this.getContext(),getString(R.string.inicio_sesion_exitoso), Toast.LENGTH_LONG).show();
                    Log.d("ID_Usuario",authResult.getUser().getUid());
                    //TODO Pasar a la pestaña de inicio una vez entrado

                    //Si fuera otra actividad:
                    ///startActivity(new Intent(LoginFragment.this.getContext(), MainActivity.class));
                    ///LoginFragment.this.getActivity().finish();
                }


            } );
        }
    }
    public void cargarRegistro(){
        //TODO  Pasar a la pestaña de registro
    }
}
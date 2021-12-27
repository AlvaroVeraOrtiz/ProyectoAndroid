package com.example.proyectoandroid.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private EditText correo, password, nombre;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance(FirebaseAuth auth) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        correo = view.findViewById(R.id.correoDeRegistro);
        nombre = view.findViewById(R.id.nombreDeRegistro);
        password = view.findViewById(R.id.passwordDeRegistro);
        return view;

    }




}
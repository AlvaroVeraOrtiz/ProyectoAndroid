package com.example.proyectoandroid.ui.seguidos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.SingletonMap;
import com.example.proyectoandroid.Resources.Usuario;
import com.example.proyectoandroid.ui.watchlater.ComentadosActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    List<String> seguidos;
    private LayoutInflater mInflater;
    public UsuarioAdapter(@NonNull Context context, int resource, @NonNull List<Usuario> objects,List<Usuario> seguidos) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.seguidos = new ArrayList<>();
        for(Usuario u : seguidos){
            this.seguidos.add(u.getEmail());
            Log.d("Correo",u.getEmail());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mInflater.inflate(R.layout.list_seguidos_item, null, false);

        TextView text = (TextView) view.findViewById(R.id.usuario_item);
        Usuario u = this.getItem(position);
        SpannableString t = new SpannableString(u.getEmail());

        int len = u.getEmail().length();
        t.setSpan(new StyleSpan(Typeface.BOLD), 0, len , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.setSpan(new ForegroundColorSpan(color(u.getUid())),0,len,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(t);
        ImageButton b = (ImageButton) view.findViewById(R.id.videos_comentarios);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cambiar tipo de activity
                Intent intento = new Intent(getContext(), ComentadosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idUsuario",u.getUid());
                intento.putExtras(bundle);
                getContext().startActivity(intento);

            }
        });




        ImageButton ib = (ImageButton) view.findViewById(R.id.seguir_button);

        if(!seguidos.contains(u.getEmail())){
            ib.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_person_add_24));
            Log.d("El correo",u.getEmail());

        }
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(!seguidos.contains(u.getEmail())){
                    button.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_close_24));
                    button.setEnabled(false);
                    addSeguido(u, button);
                }else{
                    button.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_person_add_24));
                    button.setEnabled(false);
                    deleteSeguido(u, button);
                }

            }
        });
        return view;
    }

    private void addSeguido(Usuario u, ImageButton button) {
        seguidos.add(u.getEmail());
        SingletonMap sm = SingletonMap.getInstance();
        Usuario usuario = (Usuario) sm.get("usuario");

        Map<String,Object> data = new HashMap<>();
        data.put("email",u.getEmail());
        data.put("nombre",u.getNombre());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(usuario.getUid())
                .collection("seguidos")
                .document(u.getUid())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), getContext().getString(R.string.envio_exitoso), Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), getContext().getString(R.string.envio_fallido), Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    }
                });



    }

    private void deleteSeguido(Usuario u, ImageButton button) {
        seguidos.remove(u.getEmail());
        SingletonMap sm = SingletonMap.getInstance();
        Usuario usuario = (Usuario) sm.get("usuario");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(usuario.getUid())
                .collection("seguidos")
                .document(u.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), getContext().getString(R.string.envio_exitoso), Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), getContext().getString(R.string.envio_fallido), Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    }
                });
    }

    private int color(String s){
        int min = 0xff0000ff;
        int max = 0xffffff00;
        return (s.hashCode()%(max-min))+min;
    }
}

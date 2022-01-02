package com.example.proyectoandroid.ui.seguidos;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.Resources.Mensajes;
import com.example.proyectoandroid.Resources.Usuario;

import java.util.List;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    private LayoutInflater mInflater;
    public UsuarioAdapter(@NonNull Context context, int resource, @NonNull List<Usuario> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        return view;
    }


    private int color(String s){
        int min = 0xff0000ff;
        int max = 0xffffff00;
        return (s.hashCode()%(max-min))+min;
    }
}

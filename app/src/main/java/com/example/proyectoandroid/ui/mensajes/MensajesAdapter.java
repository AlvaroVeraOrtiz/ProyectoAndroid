package com.example.proyectoandroid.ui.mensajes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.proyectoandroid.R;
import androidx.annotation.NonNull;

import com.example.proyectoandroid.Resources.Mensajes;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MensajesAdapter extends ArrayAdapter<Mensajes> {
    private LayoutInflater mInflater;
    private int mResource;
    //private MensajeFilter filter;
    //private List<Mensajes> originales;

    public MensajesAdapter(@NonNull Context context, int resource, @NonNull List<Mensajes> objects) {
        super(context, resource, objects);
        mResource = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mInflater.inflate(R.layout.list_mensajes_item, null, false);

        TextView text = (TextView) view.findViewById(R.id.mensaje_item);
        Mensajes m = this.getItem(position);
        String mom = " "+momento(m.getMomento());
        SpannableString t = new SpannableString(m.getNombreCreador()+mom+": "+ m.getContenido());

        int len = m.getNombreCreador().length();
        int len2 = mom.length();
        t.setSpan(new StyleSpan(Typeface.BOLD), 0, len + len2 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.setSpan(new ForegroundColorSpan(color(m.getCreador())),0,len,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.setSpan(new ForegroundColorSpan(Color.GRAY), len, len + len2 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(t);

        return view;
    }

    private String momento(long momento){
        StringJoiner res= new StringJoiner(":","(",")");
        momento = momento/1000;

        long m = momento/3600;
        res.add(m<10?"0":""+m);

        m = (momento%3600)/60;
        res.add(m<10?"0":""+m);

        m = momento%60;
        res.add(m<10?"0":""+m);

        return res.toString();
    }

    private int color(String s){
        int min = 0xff0000ff;
        int max = 0xffffff00;
        return (s.hashCode()%(max-min))+min;
    }


    /*
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new MensajeFilter();
        }
        return filter;
    }
    private class MensajeFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            int value = 0;
            try{
                value = Integer.parseInt(constraint.toString());
                ArrayList<Mensajes> filteredItems = new ArrayList<>();
                for(int i = 0; i<originales.size();i++){
                    if(originales.get(i).getMomento()<=value){
                        filteredItems.add(originales.get(i));
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;

            }catch (Exception e){

                result.values = originales;
                result.count = originales.size();

            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            filtrados = (ArrayList<Mensajes>)results.values;
            addAll((ArrayList<Mensajes>)results.values);
            notifyDataSetChanged();
        }

    }*/
}

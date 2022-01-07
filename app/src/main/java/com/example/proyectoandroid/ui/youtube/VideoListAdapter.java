package com.example.proyectoandroid.ui.youtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectoandroid.R;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

import java.util.List;

//Adaptador personalizado para los videos de youtube
public class VideoListAdapter extends BaseAdapter {
    private List<Video> products;
    private Context context;

    public VideoListAdapter(Context context, List<Video> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Carga el adaptador de los items para la listView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.list_view_item,null);
        ImageView image = view.findViewById(R.id.video_thumbnail);
        Video p = products.get(position);
        Glide.with(context).load("https://img.youtube.com/vi/" + p.getId() + "/0.jpg").into(image);
        TextView text = view.findViewById(R.id.video_title);
        text.setText(p.getSnippet().getTitle());

        return view;
    }
}

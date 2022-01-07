package com.example.proyectoandroid.ui.youtube;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.example.proyectoandroid.R;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

import java.util.List;

public class ResultAdapter extends BaseAdapter {
    private List<SearchResult> products;
    private Context context;

    public ResultAdapter (Context context, List<SearchResult> products) {
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

    //Para cada elemento del adaptador, carga una imagen y un texto
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.list_view_item,null);
        ImageView image = view.findViewById(R.id.video_thumbnail);
        SearchResult p = products.get(position);
        Glide.with(context).load("https://img.youtube.com/vi/" + p.getId().getVideoId() + "/0.jpg").into(image);
        TextView text = (TextView) view.findViewById(R.id.video_title);

        Spanned s = Html.fromHtml(p.getSnippet().getTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY);
        text.setText(s);

        return view;
    }
}

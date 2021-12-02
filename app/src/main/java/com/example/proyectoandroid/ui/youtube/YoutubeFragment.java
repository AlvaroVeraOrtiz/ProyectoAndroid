package com.example.proyectoandroid.ui.youtube;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoandroid.YoutubeActivity;
import com.example.proyectoandroid.databinding.FragmentYoutubeBinding;

public class YoutubeFragment extends Fragment {

    private FragmentYoutubeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentYoutubeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.ptText.setText("Buenos dias");
        binding.btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(getActivity(), YoutubeActivity.class);
                String video = binding.ptText.getText().toString();
                intento.putExtra("video",video);

                intento.setAction(Intent.ACTION_SEND);
                intento.setType("text/plain");
                startActivity(intento);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
package com.example.proyectoandroid.Resources;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class Mensajes {
    private String creador;
    private String nombreCreador;
    private String momento;
    private String video;

    public String getCreador() {
        return creador;
    }

    public String getNombreCreador() {
        return nombreCreador;
    }

    public String getMomento() {
        return momento;
    }

    public String getVideo() {
        return video;
    }

    public Mensajes(String creador, String nombreCreador, Timestamp momento, String video) {
        this.creador = creador;
        this.nombreCreador = nombreCreador;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        this.momento = formatter.format(momento.toDate());
        this.video = video;
    }
}

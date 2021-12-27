package com.example.proyectoandroid.Resources;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class Mensajes {
    private String creador;
    private String nombreCreador;
    private Long momento;
    private String video;

    public String getCreador() {
        return creador;
    }

    public String getNombreCreador() {
        return nombreCreador;
    }

    public Long getMomento() {
        return momento;
    }

    public String getVideo() {
        return video;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public void setNombreCreador(String nombreCreador) {
        this.nombreCreador = nombreCreador;
    }

    public void setMomento(Long momento) {
        this.momento = momento;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Mensajes(String creador, String nombreCreador, Long momento, String video) {
        this.creador = creador;
        this.nombreCreador = nombreCreador;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        this.momento = momento;
        this.video = video;
    }
}

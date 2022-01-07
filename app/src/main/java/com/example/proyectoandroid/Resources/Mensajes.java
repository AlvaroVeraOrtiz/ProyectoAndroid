package com.example.proyectoandroid.Resources;

import android.os.Message;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
//Un objeto de esta clase representa a un mensaje en la base de datos.
public class Mensajes implements Comparable<Mensajes> {
    private String creador;
    private String contenido;
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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Mensajes(String creador, String contenido, String nombreCreador, Long momento, String video){
        this.creador = creador;
        this.nombreCreador = nombreCreador;
        this.momento = momento;
        this.video = video;
        this.contenido = contenido;
    }

    public Mensajes() {
    }

    @Override
    public int compareTo(Mensajes o) {
        long res = momento-o.getMomento();
        return (int)res;
    }
}

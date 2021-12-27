package com.example.proyectoandroid.Resources;

public class Usuario {
    private String uid="";
    private String email="";
    private String nombre="";

    public Usuario(){

    }
    public Usuario(String email, String nombre) {
        this.email = email;
        this.nombre = nombre;
    }

    public Usuario(String uid, String email, String nombre) {
        this.uid = uid;
        this.email = email;
        this.nombre = nombre;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

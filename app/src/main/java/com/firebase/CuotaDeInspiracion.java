package com.firebase;

import com.firetest.R;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by emanuel on 29/06/18.
 */
public class CuotaDeInspiracion implements Serializable{

    private String autor;
    private String frase;
    private String propietario;
    private int calificacion;
    private int votos;
    private String fecha;

    public CuotaDeInspiracion() {
    }

    public CuotaDeInspiracion(String autor, String frase) {
        this.autor = autor;
        this.frase = frase;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    @Override
    public String toString() {
        return autor + " - "+ propietario;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}


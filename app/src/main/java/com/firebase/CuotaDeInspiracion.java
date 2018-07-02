package com.firebase;

import com.firetest.R;

/**
 * Created by emanuel on 29/06/18.
 */
public class CuotaDeInspiracion{

    private String autor;
    private String frase;

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
        return autor + " : " + frase;
    }

}


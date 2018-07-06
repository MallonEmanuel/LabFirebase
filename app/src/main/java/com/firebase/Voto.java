package com.firebase;

import java.io.Serializable;

/**
 * Created by emanuel on 05/07/18.
 */
public class Voto implements Serializable{
    private String email;
    private int voto;

    public Voto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }
}

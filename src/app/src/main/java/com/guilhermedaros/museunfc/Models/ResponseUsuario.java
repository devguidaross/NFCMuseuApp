package com.guilhermedaros.museunfc.Models;

import com.guilhermedaros.museunfc.Entities.Usuario;

public class ResponseUsuario extends Response {

    private Usuario data = null;

    public Usuario getData() {
        return data;
    }

    public void setData(Usuario data) {
        this.data = data;
    }
}

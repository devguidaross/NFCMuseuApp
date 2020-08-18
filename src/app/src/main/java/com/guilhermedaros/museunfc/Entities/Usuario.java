package com.guilhermedaros.museunfc.Entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Usuario extends RealmObject {

    @PrimaryKey
    private int id;
    private String login;
    private String senha;

    public Usuario(int aId, String aNome, String asenha) {
        this.id = aId;
        this.login = aNome;
        this.senha = asenha;
    }

    public Usuario() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

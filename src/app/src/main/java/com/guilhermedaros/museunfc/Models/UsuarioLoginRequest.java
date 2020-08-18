package com.guilhermedaros.museunfc.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioLoginRequest {
    private String login;
    private String senha;

    public UsuarioLoginRequest(String aLogin, String aSenha) {
        this.login = aLogin;
        this.senha = aSenha;
    }

    public JSONObject toJSON() throws JSONException {

        JSONObject jo = new JSONObject();
        jo.put("login", login);
        jo.put("senha", senha);

        return jo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String aLogin) {
        this.login = aLogin;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String aSenha) {
        this.senha = senha;
    }
}

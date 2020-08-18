package com.guilhermedaros.museunfc.Models;

import com.guilhermedaros.museunfc.Entities.ItemMuseu;

import java.util.List;

public class Response {

    private Boolean sucesso;
    private String mensagem;
    private Integer codigo;

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}

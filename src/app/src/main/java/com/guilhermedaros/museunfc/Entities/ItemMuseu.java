package com.guilhermedaros.museunfc.Entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemMuseu extends RealmObject {

    @PrimaryKey
    private int id;
    private String nome;
    private String ordem;
    private String familia;
    private String descricao;
    private String distribuicaoGeografica;
    private String habitat;
    private String habitosAlimentares;
    private String reproducao;
    private String periodoDeVida;
    private boolean ativo;

    public ItemMuseu(int aId, String aNome, String aordem, String afamilia, String adescricao, String adistribuicaoGeografica, String ahabitat, String ahabitosAlimentares, String areproducao, String aperiodoDeVida, boolean aativo) {
        this.id = aId;
        this.nome = aNome;
        ordem = aordem;
        familia = afamilia;
        descricao = adescricao;
        distribuicaoGeografica = adistribuicaoGeografica;
        habitat = ahabitat;
        habitosAlimentares = ahabitosAlimentares;
        reproducao = areproducao;
        periodoDeVida = aperiodoDeVida;
        ativo = aativo;
    }

    public ItemMuseu() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getOrdem() {
        return this.ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public String getFamilia() {
        return this.familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDistribuicaoGeografica() {
        return this.distribuicaoGeografica;
    }

    public void setDistribuicaoGeografica(String distribuicaoGeografica) {
        this.distribuicaoGeografica = distribuicaoGeografica;
    }

    public String getHabitat() {
        return this.habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getHabitosAlimentares() {
        return this.habitosAlimentares;
    }

    public void setHabitosAlimentares(String habitosAlimentares) {
        this.habitosAlimentares = habitosAlimentares;
    }

    public String getReproducao() {
        return this.reproducao;
    }

    public void setReproducao(String reproducao) {
        this.reproducao = reproducao;
    }

    public String getPeriodoDeVida() {
        return this.periodoDeVida;
    }

    public void setPeriodoDeVida(String periodoDeVida) {
        this.periodoDeVida = periodoDeVida;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}

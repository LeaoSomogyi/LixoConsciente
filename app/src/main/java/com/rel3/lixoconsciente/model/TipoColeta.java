package com.rel3.lixoconsciente.model;

/**
 * Created by Felipe on 12/10/2016.
 */
public class TipoColeta {

    private int codigo;
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String toString(){
        return this.getDescricao();
    }
}

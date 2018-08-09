package com.rel3.lixoconsciente.model;

/**
 * Created by Felipe on 11/10/2016.
 */
public class Coleta {

    private int empresaId;
    private String nome;
    private boolean cobraColeta;
    private boolean coletaDomicilio;
    private String tipoFone;
    private String fone;
    private String site;
    private String email;
    private String endereco;
    private String numero;
    private double latitude;
    private double longitude;
    private int tipoColetaId;
    private String tipoColeta;

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isCobraColeta() {
        return cobraColeta;
    }

    public void setCobraColeta(boolean cobraColeta) {
        this.cobraColeta = cobraColeta;
    }

    public boolean isColetaDomicilio() {
        return coletaDomicilio;
    }

    public void setColetaDomicilio(boolean coletaDomicilio) {
        this.coletaDomicilio = coletaDomicilio;
    }

    public String getTipoFone() {
        return tipoFone;
    }

    public void setTipoFone(String tipoFone) {
        this.tipoFone = tipoFone;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getTipoColetaId() {
        return tipoColetaId;
    }

    public void setTipoColetaId(int tipoColetaId) {
        this.tipoColetaId = tipoColetaId;
    }

    public String getTipoColeta() {
        return tipoColeta;
    }

    public void setTipoColeta(String tipoColeta) {
        this.tipoColeta = tipoColeta;
    }
}

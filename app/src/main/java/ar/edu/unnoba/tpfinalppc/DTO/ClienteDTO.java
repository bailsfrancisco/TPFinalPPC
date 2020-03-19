package ar.edu.unnoba.tpfinalppc.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClienteDTO {

    @Expose
    @SerializedName("distancia")
    private double distancia;
    @Expose
    @SerializedName("tipo")
    private String tipo;
    @Expose
    @SerializedName("detalle")
    private String detalle;
    @Expose
    @SerializedName("valor")
    private int valor;
    @Expose
    @SerializedName("telefono")
    private String telefono;
    @Expose
    @SerializedName("domicilio")
    private String domicilio;
    @Expose
    @SerializedName("longitud")
    private double longitud;
    @Expose
    @SerializedName("latitud")
    private double latitud;
    @Expose
    @SerializedName("descripcion")
    private String descripcion;

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString(){
        return descripcion;
    }
}

package ar.edu.unnoba.tpfinalppc.Model;

public class Cliente {

    int id;
    private String descripcion;
    private String tipo;
    private String detalle;
    private Double distancia;
    private String domicilio;
    private Double latitud;
    private Double longitud;
    private Long telefono;
    private Double valor;
    private int referenceImage;

    public Cliente() {
    }

    public Cliente(int id, String descripcion, String tipo, String detalle, Double distancia, String domicilio, Long telefono, Double valor) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.detalle = detalle;
        this.distancia = distancia;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getReferenceImage() {
        return referenceImage;
    }

    public void setReferenceImage(int referenceImage) {
        this.referenceImage = referenceImage;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "descripcion='" + descripcion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", detalle='" + detalle + '\'' +
                ", distancia=" + distancia +
                ", domicilio='" + domicilio + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", telefono=" + telefono +
                ", valor=" + valor +
                '}';
    }
}

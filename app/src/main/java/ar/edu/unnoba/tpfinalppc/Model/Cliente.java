package ar.edu.unnoba.tpfinalppc.Model;

public class Cliente implements  Comparable<Cliente>{

    private String descripcion;
    private Double latitud;
    private Double longitud;
    private String domicilio;
    private Long telefono;
    private Double valor;
    private String detalle;
    private String tipo;
    private float distancia;

    private int image;

    public Cliente() {
    }

    public Cliente(String descripcion, Double latitud, Double longitud, String domicilio, Long telefono, Double valor, String detalle, String tipo, float distancia, int image) {
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.valor = valor;
        this.detalle = detalle;
        this.tipo = tipo;
        this.distancia = distancia;
        this.image = image;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "descripcion='" + descripcion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", domicilio='" + domicilio + '\'' +
                ", telefono=" + telefono +
                ", valor=" + valor +
                ", detalle='" + detalle + '\'' +
                ", tipo='" + tipo + '\'' +
                ", distancia=" + distancia +
                ", image=" + image +
                '}';
    }

    @Override
    public int compareTo(Cliente o) {
        if (getDistancia() > o.getDistancia()) {
            return 1;
        }
        else if (getDistancia() <  o.getDistancia()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}

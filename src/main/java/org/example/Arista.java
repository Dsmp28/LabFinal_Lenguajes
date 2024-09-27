package org.example;

public class Arista {
    private Estado estadoOrigen;
    private Estado estadoDestino;
    private String condicion;

    public Arista(Estado estadoOrigen, Estado estadoDestino, String condicion) {
        this.estadoOrigen = estadoOrigen;
        this.estadoDestino = estadoDestino;
        this.condicion = condicion;
    }

    public Estado getEstadoOrigen() {
        return estadoOrigen;
    }

    public Estado getEstadoDestino() {
        return estadoDestino;
    }

    public String getCondicion() {
        return condicion;
    }
}

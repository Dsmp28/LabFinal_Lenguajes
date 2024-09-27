package org.example;

import java.util.List;
import java.util.ArrayList;

public class Grafo {
    private List<Estado> estados;
    private List<Arista> aristas;

    public Grafo() {
        this.estados = new ArrayList<>();
        this.aristas = new ArrayList<>();
    }

    public void agregarEstado(Estado estado) {
        estados.add(estado);
    }

    public void agregarArista(Arista arista) {
        aristas.add(arista);
    }

    public List<Arista> getAristas() {
        return aristas;
    }
}

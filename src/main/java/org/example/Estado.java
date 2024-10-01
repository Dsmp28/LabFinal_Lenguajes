package org.example;

public class Estado {
    private final int id;
    private String nombre;
    private boolean epsilon;

    public Estado(int id, String nombre, boolean epsilon) {
        this.id = id;
        this.nombre = nombre;
        this.epsilon = epsilon;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean esEpsilon() {
        return epsilon;
    }
}
